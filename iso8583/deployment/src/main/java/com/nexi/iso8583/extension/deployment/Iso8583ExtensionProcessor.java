package com.nexi.iso8583.extension.deployment;

import com.nexi.iso8583.extension.runtime.ISOSerializer;
import com.nexi.iso8583.extension.runtime.InvalidCreditCardPanException;
import com.nexi.iso8583.extension.runtime.MessageOriginDomainType;
import com.nexi.iso8583.extension.runtime.field.ASCIITLLLVList;
import io.quarkus.arc.deployment.GeneratedBeanBuildItem;
import io.quarkus.arc.deployment.GeneratedBeanGizmoAdaptor;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ApplicationIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.gizmo.AssignableResultHandle;
import io.quarkus.gizmo.BranchResult;
import io.quarkus.gizmo.BytecodeCreator;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import io.quarkus.gizmo.ResultHandle;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.ClassType;
import org.jboss.jandex.DotName;
import org.jboss.jandex.FieldInfo;
import org.jboss.jandex.Index;
import org.jboss.jandex.MethodInfo;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.List;

import static com.nexi.iso8583.extension.deployment.ISO8583DotNames.ACCOUNT_VERIFICATION_DOT_NAME;
import static com.nexi.iso8583.extension.deployment.ISO8583DotNames.COMPOSITE_ISO_FIELD_DOT_NAME;
import static com.nexi.iso8583.extension.deployment.ISO8583DotNames.ECOMMERCE_DOT_NAME;
import static com.nexi.iso8583.extension.deployment.ISO8583DotNames.ISO_FIELD_DOT_NAME;
import static com.nexi.iso8583.extension.deployment.ISO8583DotNames.ISO_SUB_FIELD_DOT_NAME;
import static com.nexi.iso8583.extension.deployment.ISO8583DotNames.LUHN_CKECK_DIGIT_DOT_NAME;
import static com.nexi.iso8583.extension.deployment.ISO8583DotNames.MOTO_DOT_NAME;
import static com.nexi.iso8583.extension.deployment.ISO8583DotNames.REFUND_DOT_NAME;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;

class Iso8583ExtensionProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Iso8583ExtensionProcessor.class);
    public static final String QUARKUS_ISOSERIALIZER = "$quarkusISOSerializer";
    private static final String FEATURE = "iso8583-extension";
    public static final String SERIALIZE_METHOD_NAME = "serialize";
    public static final String SET_METHOD_NAME = "set";
    public static final String SET_PACKAGER_METHOD_NAME = "setPackager";
    public static final String SET_MTI_METHOD_NAME = "setMTI";
    public static final String GETTER_METHOD_PREFIX = "get";
    public static final String CHECK_LUHN_METHOD_NAME = "checkLuhn";
    public static final String PAD_LEFT_METHOD_NAME = "padLeft";
    public static final String PACK_METHOD_NAME = "pack";
    public static final String ADD_METHOD_NAME = "add";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void generateSerializer(ApplicationIndexBuildItem jandex, BuildProducer<GeneratedBeanBuildItem> generatedBeanBuildItemBuildProducer) {
        Index index = jandex.getIndex();

        List<AnnotationInstance> annotationInstanceList = index.getAnnotations(ISO8583DotNames.ISO8583_VERSION_DOT_NAME);

        for (AnnotationInstance annotationInstance : annotationInstanceList) {

            String messageTypeIndicator = annotationInstance.values().get(0).asString();
            ClassInfo classInfo = annotationInstance.target().asClass();
            String pojoClassName = classInfo.name().toString();
            String generatedClassName = pojoClassName + QUARKUS_ISOSERIALIZER;

            AnnotationInstance authorizationRequestAnnotation = classInfo.annotation(ISO8583DotNames.AUTHORIZATION_REQUEST_DOT_NAME);
            AnnotationInstance messageOriginAnnotation = classInfo.annotation(ISO8583DotNames.MESSAGE_ORIGIN_DOT_NAME);

            if (authorizationRequestAnnotation != null) {
                messageTypeIndicator = messageTypeIndicator + "10";
            }

            if(messageOriginAnnotation != null && messageOriginAnnotation.value().asEnum().equals(MessageOriginDomainType.ACQUIRER.name())) {
                messageTypeIndicator = messageTypeIndicator + "0";
            }

            String processingCode;

            if(classInfo.hasAnnotation(REFUND_DOT_NAME)){
                processingCode = "200000";
            }else if(classInfo.hasAnnotation(ACCOUNT_VERIFICATION_DOT_NAME)){
                processingCode = "360000";
            }else {
                processingCode = "000000";
            }

            GeneratedBeanGizmoAdaptor gizmoAdapter = new GeneratedBeanGizmoAdaptor(generatedBeanBuildItemBuildProducer);

            try (ClassCreator classCreator = ClassCreator.builder()
                    .className(generatedClassName)
                    .interfaces(ISOSerializer.class)
                    .classOutput(gizmoAdapter)
                    .build()) {

                classCreator.addAnnotation(ApplicationScoped.class);

                MethodCreator serialize = classCreator.getMethodCreator(SERIALIZE_METHOD_NAME, byte[].class, Object.class, ISOPackager.class);
                serialize.setModifiers(ACC_PUBLIC);

                AssignableResultHandle isoMsgVariable = serialize.createVariable(ISOMsg.class);
                ResultHandle casted = serialize.checkCast(serialize.getMethodParam(0), pojoClassName);

                //ISOMsg var3 = new ISOMsg();
                ResultHandle newInstance = serialize.newInstance(MethodDescriptor.ofConstructor(ISOMsg.class));
                serialize.assign(isoMsgVariable, newInstance);

                MethodDescriptor isoMsgSetMethodDescriptor = MethodDescriptor
                            .ofMethod(
                                    ISOMsg.class.getName(),
                                    SET_METHOD_NAME,
                                    void.class,
                                    int.class,
                                    String.class
                            );

                //var3.setPackager(var);
                MethodDescriptor setPackager = MethodDescriptor
                        .ofMethod(
                                ISOMsg.class.getName(),
                                SET_PACKAGER_METHOD_NAME,
                                void.class,
                                ISOPackager.class
                        );

                ResultHandle packager = serialize.getMethodParam(1);
                serialize.invokeVirtualMethod(setPackager, isoMsgVariable, packager);

                //msg.setMTI("1100");
                MethodDescriptor setMTIDescriptor = MethodDescriptor
                        .ofMethod(
                                ISOMsg.class.getName(),
                                SET_MTI_METHOD_NAME,
                                void.class,
                                String.class
                        );

                ResultHandle mti = serialize.load(messageTypeIndicator);
                serialize.invokeVirtualMethod(setMTIDescriptor, isoMsgVariable, mti);

                List<AnnotationInstance> fields = classInfo.annotations(ISO_FIELD_DOT_NAME);

                boolean isProcessingCodeField = fields.stream()
                        .map(an -> an.values().get(0).asInt())
                        .anyMatch(integer -> integer == 3);

                if(!isProcessingCodeField) {
                    serialize.invokeVirtualMethod(isoMsgSetMethodDescriptor, isoMsgVariable, serialize.load(3), serialize.load(processingCode));
                }

                for (AnnotationInstance fieldAnnotation : fields) {

                    int isoFieldNumber = fieldAnnotation.values().get(0).asInt();
                    FieldInfo fieldInfo = fieldAnnotation.target().asField();

                    String fieldName = fieldInfo.name();
                    String getterMethod = GETTER_METHOD_PREFIX + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                    DotName actualFieldType = fieldInfo.type().name();

                    if(actualFieldType.equals(DotName.createSimple(String.class))){
                        //Simple String field
                        MethodDescriptor getterDescriptor = MethodDescriptor
                                .ofMethod(
                                        pojoClassName,
                                        getterMethod,
                                        String.class
                                );

                        ResultHandle value = serialize.invokeVirtualMethod(getterDescriptor, casted);
                        ResultHandle number = serialize.load(isoFieldNumber);

                        //pan validation
                        if(isoFieldNumber == 2 && fieldInfo.hasAnnotation(LUHN_CKECK_DIGIT_DOT_NAME)) {
                            MethodDescriptor luhnMethodDescriptor = MethodDescriptor
                                    .ofMethod(
                                            ISOSerializer.class,
                                            CHECK_LUHN_METHOD_NAME,
                                            boolean.class,
                                            String.class
                                    );
                            ResultHandle checked = serialize.invokeSpecialInterfaceMethod(luhnMethodDescriptor, serialize.getThis(), value);
                            BranchResult checkedBranch = serialize.ifTrue(checked);
                            BytecodeCreator noValid = checkedBranch.falseBranch();
                            noValid.throwException(InvalidCreditCardPanException.class, "The provided credit card PAN is invalid.");
                        }

                        //if value != null  => var.set(2, "5425233430109903");
                        BranchResult branchResult = serialize.ifNull(value);
                        BytecodeCreator falseBranch = branchResult.falseBranch();
                        //falseBranch.invokeVirtualMethod(isoMsgSetMethodDescriptor, isoMsgVariable, number, value);

                        if(isoFieldNumber == 4) {
                            MethodDescriptor padLeftMethodDescriptor = MethodDescriptor
                                    .ofMethod(
                                            ISOSerializer.class,
                                            PAD_LEFT_METHOD_NAME,
                                            String.class,
                                            String.class,
                                            int.class,
                                            char.class
                                    );
                            BytecodeCreator falseAmountBranch = branchResult.falseBranch();
                            ResultHandle amountValue = falseAmountBranch.invokeVirtualMethod(getterDescriptor, casted);
                            ResultHandle padded = falseAmountBranch.invokeSpecialInterfaceMethod(padLeftMethodDescriptor, serialize.getThis(), amountValue, serialize.load(12), serialize.load('0'));
                            falseAmountBranch.invokeVirtualMethod(isoMsgSetMethodDescriptor, isoMsgVariable, serialize.load(4), padded);
                        } else {
                            falseBranch.invokeVirtualMethod(isoMsgSetMethodDescriptor, isoMsgVariable, number, value);
                        }

                        if(isoFieldNumber == 22) {
                            BytecodeCreator trueBranch = branchResult.trueBranch();
                            if(classInfo.hasAnnotation(MOTO_DOT_NAME)) {
                                trueBranch.invokeVirtualMethod(isoMsgSetMethodDescriptor, isoMsgVariable, serialize.load(22), serialize.load("100020100100"));
                            }else if(classInfo.hasAnnotation(ECOMMERCE_DOT_NAME)) {
                                trueBranch.invokeVirtualMethod(isoMsgSetMethodDescriptor, isoMsgVariable, serialize.load(22), serialize.load("100050J00100"));
                            }
                        }

                        if(isoFieldNumber == 37) {
                            BytecodeCreator trueBranch = branchResult.trueBranch();

                            MethodDescriptor rrnDescriptor = MethodDescriptor
                                    .ofMethod(
                                            ISOSerializer.class,
                                            "rrn",
                                            String.class
                                    );

                            ResultHandle rrn = trueBranch.invokeSpecialMethod(rrnDescriptor, serialize.getThis());
                            trueBranch.invokeVirtualMethod(isoMsgSetMethodDescriptor, isoMsgVariable, number, rrn);
                        }
                    }else {
                        //Composite field
                        if(fieldInfo.hasAnnotation(COMPOSITE_ISO_FIELD_DOT_NAME)){

                            ClassInfo compositeClassInfo =  index.getClassByName(fieldAnnotation.target().asField().type().name());

                            List<AnnotationInstance> subFields = compositeClassInfo.annotations(ISO_SUB_FIELD_DOT_NAME);

                            MethodDescriptor compositeGetterDescriptor = MethodDescriptor
                                    .ofMethod(
                                            pojoClassName,
                                            getterMethod,
                                            compositeClassInfo.toString()
                                    );

                            ResultHandle composite = serialize.invokeVirtualMethod(compositeGetterDescriptor, casted);

                            for (AnnotationInstance subFieldAnnotationInstance : subFields) {

                                if(isoFieldNumber == 119){

                                    int isoSubFieldNumber = subFieldAnnotationInstance.values().get(0).asInt();
                                    FieldInfo subFieldInfo = subFieldAnnotationInstance.target().asField();
                                    String subFieldName = subFieldInfo.name();
                                    String subGetterMethod = GETTER_METHOD_PREFIX + subFieldName.substring(0, 1).toUpperCase() + subFieldName.substring(1);

                                    MethodDescriptor subGetterDescriptor = MethodDescriptor
                                            .ofMethod(
                                                    compositeClassInfo.toString(),
                                                    subGetterMethod,
                                                    byte[].class
                                            );

                                    BranchResult compositeIsNullResult = serialize.ifNull(composite);

                                    BytecodeCreator subValueNullResultNotNull = compositeIsNullResult.falseBranch();

                                    //ASCIITLLLVList i119 = new ASCIITLLLVList();
                                    ResultHandle asciiList = subValueNullResultNotNull.newInstance(MethodDescriptor.ofConstructor(ASCIITLLLVList.class));
                                    ResultHandle subValue = subValueNullResultNotNull.invokeVirtualMethod(subGetterDescriptor, composite);

                                    //TODO: should check if not null
                                    ResultHandle subNumber = serialize.load(isoSubFieldNumber);

                                    BranchResult branchResult = subValueNullResultNotNull.ifNull(subValue);
                                    BytecodeCreator falseBranch = branchResult.falseBranch();

                                    MethodDescriptor subIsoMsgSetMethodDescriptor = MethodDescriptor
                                            .ofMethod(
                                                    ISOMsg.class.getName(),
                                                    SET_METHOD_NAME,
                                                    void.class,
                                                    int.class,
                                                    byte[].class
                                            );

                                    //public void add(int tag, byte[] value)
                                    MethodDescriptor subIsoFieldSetMethodDescriptor = MethodDescriptor
                                            .ofMethod(
                                                    ASCIITLLLVList.class.getName(),
                                                    ADD_METHOD_NAME,
                                                    void.class,
                                                    int.class,
                                                    byte[].class
                                            );


                                    if(isoSubFieldNumber == 1) {
                                        falseBranch.invokeVirtualMethod(subIsoFieldSetMethodDescriptor, asciiList, serialize.load(0x3031), subValue);
                                    }

//                                    if(isoSubFieldNumber == 19) {
//                                        falseBranch.invokeVirtualMethod(subIsoFieldSetMethodDescriptor, asciiList, serialize.load(0x3231), subValue);
//                                    }

                                    MethodDescriptor packSubField = MethodDescriptor
                                            .ofMethod(
                                                    ASCIITLLLVList.class.getName(),
                                                    PACK_METHOD_NAME,
                                                    byte[].class
                                            );

                                    //byte[] pack()
                                    ResultHandle packedSubField = subValueNullResultNotNull.invokeVirtualMethod(packSubField, asciiList);
                                    //  var3.set(119, var22);
                                    subValueNullResultNotNull.invokeVirtualMethod(subIsoMsgSetMethodDescriptor, isoMsgVariable, subValueNullResultNotNull.load(119), packedSubField);
                                }

                            }

                        }
                    }

                }

                MethodDescriptor packMethodDescriptor = MethodDescriptor
                        .ofMethod(
                                ISOMsg.class.getName(),
                                PACK_METHOD_NAME,
                                byte[].class
                        );

                //return var3.pack();
                ResultHandle bytePacked = serialize.invokeVirtualMethod(packMethodDescriptor, isoMsgVariable);
                serialize.returnValue(bytePacked);
            }

        }
    }


    private String fieldNameFromMethod(MethodInfo methodInfo) {
        if(!Modifier.isStatic(methodInfo.flags()) && isGetterMethod(methodInfo)){
            String methodName = methodInfo.name();
            if (methodName.startsWith("is")) {
                return methodName.substring(2, 3).toLowerCase() + methodName.substring(3);
            }
            if (methodName.startsWith("get") || methodName.startsWith("set")) {
                return methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
            }
            return methodName;
        }
        return null;
    }
    

    private boolean isGetterMethod(MethodInfo methodInfo) {
        String methodName = methodInfo.name();
        return Modifier.isPublic(methodInfo.flags()) && !Modifier.isStatic(methodInfo.flags())
                && methodInfo.parametersCount() == 0
                && (methodName.startsWith("get") || methodName.startsWith("is"));
    }

}
