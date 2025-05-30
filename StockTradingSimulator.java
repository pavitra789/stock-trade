
import java.util.*;

class Stock {
    private final String ticker;
    private double price;

    public Stock(String ticker, double initialPrice) {
        this.ticker = ticker;
        this.price = initialPrice;
    }

    public String getTicker() {
        return ticker;
    }

    public double getPrice() {
        return price;
    }

    public void updatePrice() {
        // Simulate random price change between -5% and +5%Bu
        double changePercent = (Math.random() * 10 - 5) / 100;
        price += price * changePercent;
        price = Math.round(price * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return ticker + ": $" + price;
    }
}

class Portfolio {
    private final Map<String, Integer> holdings;
    private double cash;

    public Portfolio(double initialCash) {
        this.cash = initialCash;
        this.holdings = new HashMap<>();
    }

    public void buy(Stock stock, int quantity) {
        double totalCost = stock.getPrice() * quantity;
        if (cash >= totalCost) {
            cash -= totalCost;
            holdings.put(stock.getTicker(), holdings.getOrDefault(stock.getTicker(), 0) + quantity);
            System.out.println("Bought " + quantity + " shares of " + stock.getTicker());
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    public void sell(Stock stock, int quantity) {
        String ticker = stock.getTicker();
        if (holdings.getOrDefault(ticker, 0) >= quantity) {
            cash += stock.getPrice() * quantity;
            holdings.put(ticker, holdings.get(ticker) - quantity);
            if (holdings.get(ticker) == 0) {
                holdings.remove(ticker);
            }
            System.out.println("Sold " + quantity + " shares of " + ticker);
        } else {
            System.out.println("Not enough shares to sell.");
        }
    }

    public void printPortfolio(Map<String, Stock> stockMap) {
        System.out.println("\n---- Portfolio ----");
        double totalValue = cash;
        for (String ticker : holdings.keySet()) {
            int quantity = holdings.get(ticker);
            double price = stockMap.get(ticker).getPrice();
            double value = price * quantity;
            totalValue += value;
            System.out.printf("%s: %d shares @ $%.2f = $%.2f\n", ticker, quantity, price, value);
        }
        System.out.printf("Cash: $%.2f\n", cash);
        System.out.printf("Total Portfolio Value: $%.2f\n", totalValue);
    }
}

public class StockTradingSimulator {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Map<String, Stock> stocks = new HashMap<>();
        stocks.put("AAPL", new Stock("AAPL", 150.0));
        stocks.put("GOOG", new Stock("GOOG", 2700.0));
        stocks.put("TSLA", new Stock("TSLA", 700.0));
        stocks.put("AMZN", new Stock("AMZN", 3300.0));

        Portfolio portfolio = new Portfolio(10000.0); // Start with $10,000
        System.out.println("Welcome to the Java Stock Trading Simulator!");

        boolean running = true;
        while (running) {
            // Update market prices
            for (Stock stock : stocks.values()) {
                stock.updatePrice();
            }

            // Display market
            System.out.println("\n---- Stock Market ----");
            for (Stock stock : stocks.values()) {
                System.out.println(stock);
            }

            portfolio.printPortfolio(stocks);

            System.out.println("\nCommands: BUY, SELL, EXIT");
            System.out.print("Enter command: ");
            String command = scanner.nextLine().toUpperCase();

            switch (command) {
                case "BUY":
                    tradeStock("BUY", stocks, portfolio);
                    break;
                case "SELL":
                    tradeStock("SELL", stocks, portfolio);
                    break;
                case "EXIT":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid command.");
            }
        }

        System.out.println("Thanks for trading. Goodbye!");
    }

    private static void tradeStock(String action, Map<String, Stock> stocks, Portfolio portfolio) {
        System.out.print("Enter stock ticker (AAPL, GOOG, TSLA, AMZN): ");
        String ticker = scanner.nextLine().toUpperCase();

        if (!stocks.containsKey(ticker)) {
            System.out.println("Invalid stock ticker.");
            return;
        }

        System.out.print("Enter quantity: ");
        int quantity;
        try {
            quantity = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity.");
            return;
        }

        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }

        Stock stock = stocks.get(ticker);
        if (action.equals("BUY")) {
            portfolio.buy(stock, quantity);
        } else {
            portfolio.sell(stock, quantity);
        }
    }
}
