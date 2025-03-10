import com.example.DeliveryService;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("Тестирование сервиса расчета доставки")
public class DeliveryServiceTests {
    private final DeliveryService service = new DeliveryService();

    @Description("Разные варианты комбинаций параметров доставки")
    @DataProvider(name = "deliveryTestCases")
    public Object[][] deliveryTestCases() {
        return new Object[][]{
                {0.1, DeliveryService.Size.SMALL, false, DeliveryService.WorkloadLevel.NORMAL, 400},
                {1.9, DeliveryService.Size.LARGE, true, DeliveryService.WorkloadLevel.NORMAL, 550},
                {2, DeliveryService.Size.SMALL, false, DeliveryService.WorkloadLevel.INCREASED, 400},
                {1.5, DeliveryService.Size.LARGE, true, DeliveryService.WorkloadLevel.HIGH, 770},
                {0.5, DeliveryService.Size.SMALL, false, DeliveryService.WorkloadLevel.VERY_HIGH, 400},
                {2.1, DeliveryService.Size.SMALL, true, DeliveryService.WorkloadLevel.VERY_HIGH, 800},
                {5, DeliveryService.Size.LARGE, false, DeliveryService.WorkloadLevel.NORMAL, 400},
                {9.9, DeliveryService.Size.SMALL, true, DeliveryService.WorkloadLevel.INCREASED, 600},
                {10, DeliveryService.Size.LARGE, false, DeliveryService.WorkloadLevel.HIGH, 420},
                {10.1, DeliveryService.Size.LARGE, true, DeliveryService.WorkloadLevel.HIGH, 980},
                {19.1, DeliveryService.Size.SMALL, false, DeliveryService.WorkloadLevel.VERY_HIGH, 480},
                {29.9, DeliveryService.Size.LARGE, true, DeliveryService.WorkloadLevel.NORMAL, 700},
                {30, DeliveryService.Size.SMALL, false, DeliveryService.WorkloadLevel.INCREASED, 400},
                {100, DeliveryService.Size.LARGE, false, DeliveryService.WorkloadLevel.HIGH, 700},
                {30.1, DeliveryService.Size.LARGE, false, DeliveryService.WorkloadLevel.NORMAL, 500},
                {30, DeliveryService.Size.SMALL, true, DeliveryService.WorkloadLevel.INCREASED, 720},
        };
    }

    @Test(dataProvider = "deliveryTestCases", groups = {"regress", "positive"})
    public void testCombinationCases(double distance, DeliveryService.Size size, boolean isFragile, DeliveryService.WorkloadLevel workloadLevel, double expected) {
        double result = service.calculateDeliveryCost(distance, size, isFragile, workloadLevel);
        Assert.assertEquals(result, expected, 0.01);
    }

    @Description("Появление исключения для хрупких грузов при доставке более 30 км")
    @Test(expectedExceptions = IllegalArgumentException.class, groups = {"regress", "negative"})
    public void testFragileCargoExceedsDistance() {
        service.calculateDeliveryCost(30.1, DeliveryService.Size.SMALL, true, DeliveryService.WorkloadLevel.INCREASED);
    }

    @Description("Появление исключения при расстоянии 0 км")
    @Test(expectedExceptions = IllegalArgumentException.class, groups = {"regress", "negative"})
    public void testDistanceEqualZero() {
        service.calculateDeliveryCost(0, DeliveryService.Size.SMALL, true, DeliveryService.WorkloadLevel.INCREASED);
    }

    @Description("Появление исключения при расстоянии менее 0 км")
    @Test(expectedExceptions = IllegalArgumentException.class, groups = {"regress", "negative"})
    public void testDistanceEqualNegative() {
        service.calculateDeliveryCost(-1, DeliveryService.Size.SMALL, true, DeliveryService.WorkloadLevel.INCREASED);
    }

    @Description("Появление исключения при расстоянии более 100 км")
    @Test(expectedExceptions = IllegalArgumentException.class, groups = {"regress", "negative"})
    public void testDistanceMoreThenHundred() {
        service.calculateDeliveryCost(100.1, DeliveryService.Size.SMALL, false, DeliveryService.WorkloadLevel.INCREASED);
    }

}
