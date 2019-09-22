package com.exmyth.commons.validator.processor.constraint;

import com.exmyth.commons.validator.processor.ValidatorProcessor;
import com.exmyth.commons.validator.util.Validators;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author exmyth
 * @date 2019-08-25 17:51
 *
 */
public final class ConstraintProcessor extends AbstractProcessor {
    private static final Class constraintClass = Constraint.class;
    private static final String validClassName = Valid.class.getCanonicalName();
    private static final java.util.List<String> constraintList = new ArrayList<>(18);

    static {
        java.util.List<String> validatorClassNameList = Validators.getValidatorClassList().stream()
                .map(Class::getCanonicalName).collect(Collectors.toList());
        constraintList.addAll(validatorClassNameList);
    }

    private Messager messager;
    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;
    private ValidatorProcessor validatorProcessor;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
        this.validatorProcessor =  new ValidatorProcessor(treeMaker, names);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(constraintClass.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(constraintClass);
        set.forEach(element -> processElement(element));
        return true;
    }

    private void processElement(Element element) {
        JCTree jcTree = trees.getTree(element);
        if(jcTree == null){
            messager.printMessage(Diagnostic.Kind.NOTE, element.getSimpleName() + " jcTree is null");
            return;
        }
        jcTree.accept(new TreeTranslator() {
            @Override
            public void visitMethodDef(JCTree.JCMethodDecl jcMethodDecl) {
                messager.printMessage(Diagnostic.Kind.NOTE, jcMethodDecl.getName() + " has been processed");

                ListBuffer<JCTree.JCStatement> listBuffer = new ListBuffer<JCTree.JCStatement>();

                List<JCTree.JCVariableDecl> params = jcMethodDecl.getParameters();
                params.forEach(param -> {
                    param.getModifiers().getAnnotations().forEach(jca -> {
                        if(validClassName.equals(jca.type.toString())){
                            JCTree.JCExpression fieldValue = validatorProcessor.getValue(null, treeMaker.Literal(param.getName().toString()));

                            List<JCTree.JCExpression> argTypes = List.of(
                                    validatorProcessor.memberAccess(ValidatorProcessor.JAVA_LANG_OBJECT),
                                    validatorProcessor.memberAccess(ValidatorProcessor.JAVA_LANG_BOOLEAN));

                            List<JCTree.JCExpression> argValues = List.of(fieldValue, treeMaker.Literal(Boolean.TRUE));

                            JCTree.JCExpressionStatement validateOrThrow = treeMaker.Exec(treeMaker.Apply(
                                    argTypes, validatorProcessor.memberAccess(ValidatorProcessor.VALIDATE_OR_THROW), argValues));
                            listBuffer.add(validateOrThrow);

                            /*
                            JCTree.JCExpressionStatement println = treeMaker.Exec(treeMaker.Apply(
                                    List.of(validatorProcessor.memberAccess("java.lang.Integer")),
                                    validatorProcessor.memberAccess("java.lang.System.out.println"),
                                    List.of(treeMaker.Literal(validateOrThrow.toString())))
                            );
                            listBuffer.add(println);
                            */
                        } else if(constraintList.contains(jca.type.toString())) {
                            JCTree.JCExpression validatorRhs = treeMaker.ClassLiteral(jca.type);
                            JCTree.JCExpression fieldValue = validatorProcessor.getValue(null, treeMaker.Literal(param.getName().toString()));
                            JCTree.JCNewArray fieldNameArray = validatorProcessor.getNameArray(treeMaker.Literal(param.getName().toString()));

                            JCTree.JCLiteral messageRhs = null;
                            List<JCTree.JCExpression> argsList = List.nil();
                            for (JCTree.JCExpression expression : jca.getArguments()) {
                                JCTree.JCAssign jcAssign = (JCTree.JCAssign) expression;
                                JCTree.JCIdent lhs = (JCTree.JCIdent) jcAssign.lhs;
                                if (ValidatorProcessor.MESSAGE.equals(lhs.toString())) {
                                    messageRhs = (JCTree.JCLiteral) jcAssign.rhs;
                                } else {
                                    argsList = argsList.append(treeMaker.Literal(lhs.getName().toString()));
                                    if(jcAssign.rhs instanceof JCTree.JCNewArray){
                                        JCTree.JCNewArray v = (JCTree.JCNewArray) jcAssign.rhs;
                                        for(JCTree.JCExpression expr :v.elems){
                                            String argv = ((JCTree.JCLiteral) expr).getValue().toString();
                                            argsList = argsList.append(treeMaker.Literal(argv));
                                        }
                                    } else{
                                        JCTree.JCLiteral v = (JCTree.JCLiteral) jcAssign.rhs;
                                        argsList = argsList.append(treeMaker.Literal(v.getValue().toString()));
                                    }
                                }
                            }

                            if(messageRhs == null){
                                messageRhs = treeMaker.Literal("");
                            }

                            JCTree.JCNewArray argsNameArray = treeMaker.NewArray(validatorProcessor.memberAccess(ValidatorProcessor.JAVA_LANG_STRING), List.nil(), argsList);

                            List<JCTree.JCExpression> argTypes = List.of(
                                    validatorProcessor.memberAccess(ValidatorProcessor.JAVA_LANG_OBJECT),
                                    validatorProcessor.arrayAccess(ValidatorProcessor.JAVA_LANG_STRING),
                                    validatorProcessor.memberAccess(ValidatorProcessor.JAVA_LANG_STRING),
                                    validatorProcessor.memberAccess(ValidatorProcessor.JAVA_LANG_CLASS),
                                    validatorProcessor.arrayAccess(ValidatorProcessor.JAVA_LANG_STRING));

                            List<JCTree.JCExpression> argValues = List.of(fieldValue,
                                    fieldNameArray, messageRhs, validatorRhs, argsNameArray);

                            JCTree.JCExpressionStatement validateOrThrow = treeMaker.Exec(treeMaker.Apply(
                                    argTypes,
                                    validatorProcessor.memberAccess(ValidatorProcessor.VALIDATE_OR_THROW),
                                    argValues
                            ));

                            listBuffer.add(validateOrThrow);

                            /*
                            JCTree.JCExpressionStatement println = treeMaker.Exec(treeMaker.Apply(
                                    List.of(validatorProcessor.memberAccess("java.lang.Integer")),
                                    validatorProcessor.memberAccess("java.lang.System.out.println"),
                                    List.of(treeMaker.Literal(jca.type.toString())))
                            );
                            listBuffer.add(println);
                            */
                        }
                    });
                });
                List<JCTree.JCStatement> statements = jcMethodDecl.getBody().getStatements();
                for (JCTree.JCStatement stat : statements) {
                    listBuffer.append(stat);
                }
                jcMethodDecl.body.stats = listBuffer.toList();
                super.visitMethodDef(jcMethodDecl);
            }
        });
    }
}
