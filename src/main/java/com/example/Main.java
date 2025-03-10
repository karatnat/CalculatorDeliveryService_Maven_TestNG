package com.example;

public class Main {
    public static void main(String[] args) {

        DeliveryService service = new DeliveryService();
        try {
            double deliveryCost1 = service.calculateDeliveryCost(
                    1,
                    DeliveryService.Size.LARGE,
                    true,
                    DeliveryService.WorkloadLevel.HIGH);
            System.out.println("deliveryCost1 = " + deliveryCost1);
        } catch (IllegalArgumentException error) {
            System.out.println(error.getMessage());
        }

    }
}