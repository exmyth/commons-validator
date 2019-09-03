package com.exmyth.commons.validator.processor.inspect;

import com.exmyth.commons.validator.processor.ValidatorProcessor;
import com.exmyth.commons.validator.util.Validators;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.TypeTag;
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
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.exmyth.commons.validator.processor.ValidatorProcessor.*;

/**
 * @author exmyth
 * @date 2019-08-20 20:02
 * @description
 */
public final class InspectProcessor extends AbstractProcessor {
    private static final java.util.List<String> groupValidatorList = new ArrayList(2);

    private static final java.util.List<Class> classList = Arrays.asList(Inspect.class, Inspects.class);

    static {
        java.util.List<String> groupValidatorClassNameList = Validators.getGroupValidatorClassList().stream()
                .map(Class::getCanonicalName).collect(Collectors.toList());
        groupValidatorList.addAll(groupValidatorClassNameList);
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
        this.validatorProcessor = new ValidatorProcessor(treeMaker, names);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.addAll(classList.stream().map(Class::getCanonicalName).collect(Collectors.toList()));
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * //重复注解
     * @Inspects(
     * {
     * 	@Inspect(field = "world", validator = Length.class,args = "min,10,max,11"),
     * 	@Inspect(field = "email", validator = Email.class)
     * })
     *
     * //单独注解
     * @Inspect(field = "world", validator = Length.class, args = "min,10,max,11")
     *
     * @param annotations
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        classList.forEach(clazz -> {
            Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(clazz);
            set.forEach(element -> processElement(element));
        });
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
                List<JCTree.JCAnnotation> jcAnnotationList = jcMethodDecl.getModifiers().getAnnotations();
                jcAnnotationList.forEach(jca -> {
                    JCTree.JCExpression validatorRhs = null;

                    JCTree.JCExpression beanRhs = null;

                    JCTree.JCNewArray fieldArrayRhs = null;
                    JCTree.JCLiteral fieldStringRhs = null;

                    JCTree.JCNewArray argsArrayRhs = null;
                    JCTree.JCLiteral argsStringRhs = null;

                    JCTree.JCLiteral messageRhs = null;

                    for (JCTree.JCExpression expression : jca.getArguments()) {
                        JCTree.JCAssign jcAssign = (JCTree.JCAssign) expression;
                        JCTree.JCIdent lhs = (JCTree.JCIdent) jcAssign.lhs;
                        if (FIELD.equals(lhs.toString())) {
                            if(jcAssign.rhs instanceof JCTree.JCNewArray){
                                fieldArrayRhs = (JCTree.JCNewArray) jcAssign.rhs;
                            } else if(jcAssign.rhs instanceof JCTree.JCLiteral){
                                fieldStringRhs = (JCTree.JCLiteral) jcAssign.rhs;
                            }
                        }
                        if (VALIDATOR.equals(lhs.toString())) {
                            validatorRhs = jcAssign.rhs;
                        }

                        if (BEAN.equals(lhs.toString())) {
                            beanRhs = jcAssign.rhs;
                        }

                        if (ARGS.equals(lhs.toString())) {
                            if(jcAssign.rhs instanceof JCTree.JCNewArray){
                                argsArrayRhs = (JCTree.JCNewArray) jcAssign.rhs;
                            } else if(jcAssign.rhs instanceof JCTree.JCLiteral){
                                argsStringRhs = (JCTree.JCLiteral) jcAssign.rhs;
                            }
                        }

                        if (MESSAGE.equals(lhs.toString())) {
                            messageRhs = (JCTree.JCLiteral) jcAssign.rhs;
                        }
                    }

                    if (validatorRhs != null) {
                        JCTree.JCNewArray fieldValueArray;
                        JCTree.JCNewArray fieldNameArray;
                        JCTree.JCExpression fieldValue;

                        if(fieldArrayRhs != null){
                            if(!fieldArrayRhs.elems.isEmpty()){
                                fieldValue = validatorProcessor.getValue(beanRhs, (JCTree.JCLiteral) fieldArrayRhs.elems.head);
                            } else{
                                fieldValue = treeMaker.Literal(TypeTag.BOT, null);
                            }

                            fieldValueArray = validatorProcessor.getValueArray(beanRhs, fieldArrayRhs);
                            fieldNameArray = validatorProcessor.getNameArray(fieldArrayRhs);
                        } else{
                            fieldValue = validatorProcessor.getValue(beanRhs, fieldStringRhs);

                            fieldValueArray = validatorProcessor.getValueArray(beanRhs, fieldStringRhs);
                            fieldNameArray = validatorProcessor.getNameArray(fieldStringRhs);
                        }

                        JCTree.JCNewArray argsNameArray;
                        if(argsArrayRhs != null){
                            argsNameArray = validatorProcessor.getNameArray(argsArrayRhs);
                        } else {
                            argsNameArray = validatorProcessor.getNameArray(argsStringRhs);
                        }

                        List<JCTree.JCExpression> argTypes = List.of(
                                validatorProcessor.memberAccess(JAVA_LANG_OBJECT),
                                validatorProcessor.arrayAccess(JAVA_LANG_STRING),
                                validatorProcessor.memberAccess(JAVA_LANG_STRING),
                                validatorProcessor.memberAccess(JAVA_LANG_CLASS),
                                validatorProcessor.arrayAccess(JAVA_LANG_STRING));

                        if(messageRhs == null){
                            messageRhs = treeMaker.Literal("");
                        }

                        List<JCTree.JCExpression> argValues;
                        if(groupValidatorList.contains(validatorRhs.type.getTypeArguments().head.toString())){
                            argValues = List.of(fieldValueArray,
                                    fieldNameArray, messageRhs, validatorRhs, argsNameArray);
                        } else{
                            argValues = List.of(fieldValue,
                                    fieldNameArray, messageRhs, validatorRhs, argsNameArray);
                        }

                        JCTree.JCExpressionStatement validateOrThrow = treeMaker.Exec(treeMaker.Apply(
                                argTypes,
                                validatorProcessor.memberAccess(VALIDATE_OR_THROW),
                                argValues
                        ));

                        listBuffer.add(validateOrThrow);

                        /*
                        JCTree.JCExpressionStatement println = treeMaker.Exec(treeMaker.Apply(
                                List.of(memberAccess("java.lang.Integer")),
                                memberAccess("java.lang.System.out.println"),
                                List.of(treeMaker.Literal(validatorRhs.type.getTypeArguments().head.toString())))
                        );
                        listBuffer.add(println);
                        */
                    }
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
