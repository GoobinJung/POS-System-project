package posPd;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/** Reports count only completed sales, using the values captured at checkout. */
public final class Reports {
    private Reports() {}
    public static List<Object[]> dailySales(Store store, LocalDate date) {
        List<Object[]> rows = new ArrayList<>();
        for (Session session : store.getSessions()) for (Sale sale : session.getSales())
            if (sale.getDateTime().toLocalDate().equals(date))
                rows.add(new Object[] {sale.getDateTime(), session.getCashier().getNumber(),
                    session.getRegister().getNumber(), sale.calcSubTotal(), sale.calcTax(), sale.calcTotal(),
                    sale.getTotalPayments(), sale.calcChange()});
        return rows;
    }
    public static List<Object[]> cashierSales(Store store, LocalDate date) {
        Map<String, Object[]> totals = new TreeMap<>();
        for (Session session : store.getSessions()) {
            for (Sale sale : session.getSales()) {
                if (!sale.getDateTime().toLocalDate().equals(date)) continue;
                String number = session.getCashier().getNumber();
                Object[] row = totals.computeIfAbsent(number, key -> new Object[] {key,
                    session.getCashier().getPerson().getName(), 0, Money.parse("0"), Money.parse("0")});
                row[2] = (Integer) row[2] + 1;
                row[3] = ((BigDecimal) row[3]).add(sale.calcTotal());
                row[4] = ((BigDecimal) row[4]).add(sale.calcCashReceived());
            }
        }
        return new ArrayList<>(totals.values());
    }
    public static List<Object[]> itemSales(Store store, LocalDate date) {
        Map<String, Object[]> totals = new TreeMap<>();
        for (Session session : store.getSessions()) for (Sale sale : session.getSales()) {
            if (!sale.getDateTime().toLocalDate().equals(date)) continue;
            for (SaleLineItem item : sale.getSaleLineItems()) {
                Object[] row = totals.computeIfAbsent(item.getItemNumber(), key -> new Object[] {key,
                    item.getDescription(), 0, Money.parse("0")});
                row[2] = (Integer) row[2] + item.getQuantity();
                row[3] = ((BigDecimal) row[3]).add(item.calcSubTotal());
            }
        }
        return new ArrayList<>(totals.values());
    }
}
