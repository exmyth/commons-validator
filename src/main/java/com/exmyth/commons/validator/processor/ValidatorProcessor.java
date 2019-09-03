package com.exmyth.commons.validator.processor;

import com.exmyth.commons.validator.processor.inspect.Bean;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

/**
 * @author exmyth
 * @date 2019-08-25 20:24
 * @description
 */
public final class ValidatorProcessor {

    public static final String MESSAGE = "message";
    public static final String VALIDATOR = "validator";
    public static final String BEAN = "bean";
    public static final String FIELD = "field";
    public static final String ARGS = "args";

    public static final String GET = "get";

    public static final String JAVA_LANG_OBJECT = "java.lang.Object";
    public static final String VALIDATOR_VALIDATION = "com.exmyth.commons.validator.Validation";
    public static final String JAVA_LANG_STRING = "java.lang.String";
    public static final String JAVA_LANG_CLASS = "java.lang.Class";
    public static final String JAVA_LANG_BOOLEAN = "java.lang.Boolean";

    public static final String VALIDATE_OR_THROW = "com.exmyth.commons.validator.util.Validators.validateOrThrow";

    private final TreeMaker treeMaker;
    private final Names names;

    public ValidatorProcessor(TreeMaker treeMaker, Names names) {
        this.treeMaker = treeMaker;
        this.names = names;
    }

    /**
     * 根据字符串获取Name(利用Names的fromString静态方法)
     * @param str
     * @return
     */
    public Name getNameFromString(String str){
        return names.fromString(str);
    }

    /**
     * 创建 域/方法 的多级访问, 方法的标识只能是最后一个
     * 例如： java.lang.System.out.println
     * @param components
     * @return
     */
    public JCTree.JCExpression memberAccess(String components) {
        String[] componentArray = components.split("\\.");
        JCTree.JCExpression expr = treeMaker.Ident(getNameFromString(componentArray[0]));
        for (int i = 1; i < componentArray.length; i++) {
            expr = treeMaker.Select(expr, getNameFromString(componentArray[i]));
        }
        return expr;
    }

    public JCTree.JCExpression arrayAccess(String components) {
        JCTree.JCArrayTypeTree jcArrayTypeTree = treeMaker.TypeArray(memberAccess(components));
        return jcArrayTypeTree;
    }

    public JCTree.JCExpression getValue(JCTree.JCExpression beanRhs, JCTree.JCLiteral fieldStringRhs) {
        JCTree.JCExpression fieldValue;

        String name = fieldStringRhs.getValue().toString();
        int index = name.indexOf(".");
        if(index>-1){
            String var = name.substring(0, index);
            if(beanRhs == null || beanRhs.toString().endsWith(Bean.GETTER.name())){
                String method = GET+String.valueOf(name.charAt(index+1)).toUpperCase()+name.substring(index+2);
                fieldValue = treeMaker.Apply(List.nil(), memberAccess(var+"."+method), List.nil());
            } else if(beanRhs.toString().endsWith(Bean.MAPPER.name())){
                String method = GET;
                String key = name.substring(index+1);
                fieldValue = treeMaker.Apply(List.nil(), memberAccess(var+"."+method),
                        List.of(treeMaker.Literal(key)));
            } else {
                fieldValue = treeMaker.Literal(TypeTag.BOT, null);
            }
        } else {
            fieldValue = memberAccess(name);
        }
        return fieldValue;
    }

    public JCTree.JCNewArray getValueArray(JCTree.JCExpression beanRhs, JCTree.JCLiteral fieldStrRhs) {
        JCTree.JCNewArray fieldValueArray;
        List<JCTree.JCExpression> valueElems = List.nil();
        if(fieldStrRhs != null){
            valueElems = valueElems.append(getValue(beanRhs, fieldStrRhs));
        }
        fieldValueArray = treeMaker.NewArray(memberAccess(JAVA_LANG_OBJECT), List.nil(), valueElems);
        return fieldValueArray;
    }

    public JCTree.JCNewArray getValueArray(JCTree.JCExpression beanRhs, JCTree.JCNewArray fieldRhs) {
        List<JCTree.JCExpression> fieldValueList = List.nil();
        for (int i = 0; i < fieldRhs.elems.size(); i++) {
            JCTree.JCLiteral fieldLiteral = (JCTree.JCLiteral) fieldRhs.elems.get(i);
            fieldValueList = fieldValueList.append(getValue(beanRhs, fieldLiteral));
        }

        JCTree.JCNewArray fieldValueArray = treeMaker.NewArray(memberAccess(JAVA_LANG_OBJECT), fieldRhs.dims, fieldValueList);
        return fieldValueArray;
    }

    public JCTree.JCNewArray getNameArray(JCTree.JCLiteral fieldStrRhs) {
        JCTree.JCNewArray fieldNameArray;
        List<JCTree.JCExpression> nameElems = List.nil();
        if (fieldStrRhs != null) {
            String name = fieldStrRhs.getValue().toString();
            int index = name.indexOf(".");
            if (index > -1) {
                JCTree.JCLiteral item = treeMaker.Literal(name.substring(index + 1));
                nameElems = nameElems.append(item);
            } else {
                nameElems = nameElems.append(fieldStrRhs);
            }
        }
        fieldNameArray = treeMaker.NewArray(memberAccess(JAVA_LANG_STRING), List.nil(), nameElems);
        return fieldNameArray;
    }

    public JCTree.JCNewArray getNameArray(JCTree.JCNewArray fieldRhs) {
        JCTree.JCNewArray fieldNameArray;
        List<JCTree.JCExpression> nameElems = List.nil();
        for(JCTree.JCExpression expr : fieldRhs.elems){
            JCTree.JCLiteral fieldStrRhs = (JCTree.JCLiteral) expr;
            String name = fieldStrRhs.getValue().toString();
            int index = name.indexOf(".");
            if (index > -1) {
                JCTree.JCLiteral item = treeMaker.Literal(name.substring(index + 1));
                nameElems = nameElems.append(item);
            } else {
                nameElems = nameElems.append(expr);
            }
        }
        fieldNameArray = treeMaker.NewArray(memberAccess(JAVA_LANG_STRING), fieldRhs.dims, nameElems);
        return fieldNameArray;
    }
}
