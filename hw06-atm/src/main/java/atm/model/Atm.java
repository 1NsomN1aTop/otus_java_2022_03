package atm.model;

import java.util.Map;

public interface Atm {

    void putMoney(Map<BanknoteType, Integer> banknotes);

    Map<BanknoteType, Integer> getMoney(long neededSum);

    long getBalance();
}
