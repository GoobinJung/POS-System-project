# Point of Sale System

A Java desktop application that models a retail checkout workflow, from cashier login and item entry to payment handling and session summaries. Built with Swing, the project separates the user interface, business objects, and CSV data loading into distinct packages.

## Features

- **Store administration:** Maintain the store name, cashiers, registers, items, UPCs, prices, tax categories, and tax rates through dedicated management screens.
- **Cashier sessions:** Select a cashier and register, validate a demo password, and record the starting cash amount.
- **UPC-based checkout:** Enter a product UPC and quantity to add sale line items, with feedback when an item cannot be found.
- **Sale calculations:** Display subtotal, tax, total, amount tendered, and change using `BigDecimal` arithmetic. A tax-free option is available.
- **Payment entry:** Record cash, check, and credit payment details through separate Swing panels.
- **Session summaries:** Display the cashier, register, number of recorded sales, total sales, and a cash difference field.
- **Sample data import:** Load store configuration, products, cashiers, registers, and historical transaction records from `StoreData.csv`.

This is an educational POS implementation. Payment entry is simulated, and the application does not connect to a payment processor.

## Technology

| Component | Implementation |
|---|---|
| Language | Java |
| Project configuration | JavaSE-21 |
| Desktop interface | Java Swing and AWT |
| Monetary calculations | `java.math.BigDecimal` |
| Data loading | CSV with Java file I/O |
| Development environment | Eclipse IDE |
| External dependencies | None |

## Project Structure

| Path | Responsibility |
|---|---|
| [`src/posUi/`](src/posUi/) | Swing application frame, checkout screens, payment panels, and maintenance forms |
| [`src/posPd/`](src/posPd/) | Domain objects for stores, items, sales, payments, taxes, cashiers, and sessions |
| [`src/posDm/StoreDm.java`](src/posDm/StoreDm.java) | CSV import and object construction |
| [`src/posTest/StoreTest.java`](src/posTest/StoreTest.java) | Console inspection and acceptance-test demonstration methods |
| [`StoreData.csv`](StoreData.csv) | Included store and transaction data |

The domain model uses inheritance for payment types (`Cash`, `Check`, and `Credit`) and pricing (`Price` and `PromoPrice`). Java collections maintain the relationships between stores, cashiers, registers, items, sessions, and sales.

## Getting Started

### Requirements

- JDK 21, matching the Eclipse project configuration
- Eclipse IDE with Java development support, or a terminal
- Git to clone the repository, or a downloaded ZIP of the source

### Run in Eclipse

1. Clone the repository:

   ```bash
   git clone https://github.com/GoobinJung/POS-System-project.git
   ```

2. In Eclipse, select **File → Import → General → Existing Projects into Workspace**.
3. Select the cloned repository as the root directory and import the top-level **Project 2 POS SYSTEM** project. If Eclipse also lists the nested `src/posPd` project, leave it unselected.
4. Confirm that the project's **JRE System Library** uses JavaSE-21.
5. Open `src/posUi/PosFrame.java`, then select **Run As → Java Application**.

**Run with the repository root as the working directory.** The application reads `StoreData.csv` using a relative path. If the import fails, check **Run Configurations → Arguments → Working directory**.

### Run from PowerShell

From the repository root:

```powershell
$sources = Get-ChildItem -Path src -Recurse -Filter *.java |
    ForEach-Object { $_.FullName }

javac --release 21 -encoding UTF-8 -d out $sources
java -cp out posUi.PosFrame
```

The desktop entry point is **`posUi.PosFrame`**. `posUi.PosStart` runs an earlier console demonstration.

## Try the Checkout Workflow

1. Launch the application and select **POS → Login**.
2. Select one of the included demo cashiers:

   | Cashier number | Name | Password |
   |---|---|---|
   | 1 | David | `password1` |
   | 2 | Sally | `password2` |

3. Select register **1** or **2**, enter a numeric starting cash amount such as `100.00`, and log in.
4. Enter a quantity, then type a product UPC in the **Item** field and press **Enter**:

   | UPC | Product |
   |---|---|
   | `11111111111` | Turkey Sandwich |
   | `22222222222` | Ham Sandwich |
   | `33333333333` | Coke |
   | `44444444444` | Dr. Pepper |

5. Select **Payment → Cash**, enter an amount greater than the sale total, and click **Save**.
6. Select **Payment Complete**, then **Complete Sale** to begin another sale.

For the current implementation, use an amount **greater than** the total during the demo: the payment completion check uses `>` rather than `>=`.

The sample CSV contains promotional price records dated in 2025. Checkout currently selects the first price in the item's sorted price collection, so the presence of a promotion record does not imply automatic promotion selection.

## Console Inspection

After compiling, run:

```powershell
java -cp out posTest.StoreTest
```

This entry point imports `StoreData.csv` and prints store, cashier, register, item, and session information. It is a manual inspection utility, rather than an automated assertion-based test suite.

**Known issue:** With the included data, this utility prints the store, cashiers, registers, and items, then throws a `NullPointerException` while printing sessions. Imported check and credit payments do not initialize the amount tendered, which is used by `Sale.calcAmtTendered()`.

## Current Scope and Limitations

- **Persistence:** CSV import is implemented. Changes made through the interface remain in memory and are not written back to disk.
- **Payments and authentication:** Check and credit authorization methods are stubs that return `true`. Cashier passwords are stored as plain text for the demo.
- **Reporting:** The Reports menu and report panel classes are placeholders. Cash reconciliation is only partially connected to the session summary screen.
- **Transaction accounting:** Imported sale line items and the payment-completion/sale-completion workflow can add duplicate entries, affecting session totals and counts.
- **Validation and date selection:** Numeric/date input validation and selection of the applicable price or tax rate need further work.

## Development Opportunities

- Add persistent storage for configuration and completed transactions.
- Add automated tests for tax, pricing, payments, and session accounting.
- Correct exact-payment handling, duplicate additions, and date-based price/rate selection.
- Complete reporting and cash reconciliation.
- Improve form validation, password handling, and interface layout.
