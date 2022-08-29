package atm;

import atm.model.BanknoteType;
import atm.model.AtmImpl;
import atm.service.AtmService;
import atm.service.AtmServiceImpl;

import java.util.Map;

public class MainApp {
    public static void main(String[] args) {
        AtmService atmService = new AtmServiceImpl(new AtmImpl());
        atmService.putMoney(Map.of(BanknoteType.RUB1000, 15, BanknoteType.RUB200, 5));
        System.out.println("Atm balance: " + atmService.getBalance());
        var banknotes = atmService.getMoney(5000);
        for(var banknote : banknotes.entrySet()){
            System.out.printf("Banknote: %s. Count: %s\n", banknote.getKey(), banknote.getValue());
        }

        System.out.println("Atm balance: " + atmService.getBalance());
    }
}