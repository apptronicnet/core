//package net.apptronic.common.java;
//
//import net.apptronic.common.testutils.BaseTestComponent;
//import net.apptronic.common.testutils.TestContext;
//import net.apptronic.core.component.di.FactoryContext;
//import net.apptronic.core.component.di.ModuleBuilder;
//import net.apptronic.core.component.di.ModuleDefinition;
//import net.apptronic.core.component.di.ModuleKt;
//import net.apptronic.core.component.di.ObjectBuilder;
//
//import org.jetbrains.annotations.NotNull;
//import org.junit.Assert;
//import org.junit.Test;
//
//public class JavaDiTest {
//
//    private interface JavaInterface {
//
//    }
//
//    private class JavaImpl implements JavaInterface {
//
//    }
//
//    private final ModuleDefinition module = ModuleKt.declareModule(new ModuleBuilder() {
//        @Override
//        public void build(@NotNull ModuleDefinition definition) {
//            definition.declareFactory(JavaInterface.class, new ObjectBuilder<JavaInterface>() {
//                @Override
//                public JavaInterface build(@NotNull FactoryContext context) {
//                    return new JavaImpl();
//                }
//            });
//
//        }
//    });
//
//    private class Test extends BaseTestComponent {
//
//        Test(TestContext testContext) {
//            super(testContext);
//        }
//
//        final JavaInterface instance = getProvider().inject(JavaInterface.class);
//
//    }
//
//    private final TestContext testContext = new TestContext() {
//        {
//            getProvider().addModule(module);
//        }
//    };
//    private final Test testComponent = new Test(testContext);
//
//    @Test
//    public void testJava() {
//        Assert.assertNotNull(testComponent.instance);
//        Assert.assertTrue(testComponent.instance instanceof JavaImpl);
//    }
//
//}
