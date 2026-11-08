# Point of Sale System

A Java Swing desktop application for a small retail checkout workflow. The application includes cashier sessions, UPC-based sales, cash/check/credit payment simulation, store maintenance, sales reports, and local persistence.

The code is organized into separate presentation, domain, and data-management packages. It uses only the Java standard library.

## Features

- **Checkout:** Enter a UPC and quantity, remove items, apply a tax exemption, and view subtotal, tax, balance due, and change.
- **Payments:** Accept exact cash amounts, calculate change, and combine payments. Demo check and credit entries validate their required fields; non-cash overpayment is rejected.
- **Sales integrity:** Record each sale once, require full payment before completion, and preserve the unit price and tax rate captured when each line was added.
- **Pricing:** Select the latest applicable regular price and tax rate. Active promotions apply within inclusive start/end dates when cheaper than the regular price.
- **Sessions:** Record starting cash, resume an open session after restarting, and reconcile counted cash against starting cash plus net cash received from completed sales.
- **Maintenance:** Manage cashiers, registers, items, UPCs, regular/promotional prices, tax categories, and dated tax rates.
- **Reports:** View daily sales, sales by cashier, and item quantities/revenue for a selected date. Export the report to CSV.
- **Persistence:** Automatically save maintenance changes, session starts/ends, and completed sales. Keep a previous-save backup and provide a manual **File → Save** action.
- **Input handling:** Display validation messages for invalid quantities, amounts, dates, duplicate identifiers, and missing selections.
- **Passwords:** Store cashier passwords as salted PBKDF2-HMAC-SHA256 hashes in the local snapshot.

## Requirements

| Component | Requirement |
|---|---|
| Java | JDK 21 to match the Eclipse project configuration |
| IDE | Eclipse with Java development support, or a terminal |
| Dependencies | Java standard library only |
| Interface | Swing and AWT |

The source also compiles and the local regression suite has been exercised with JDK 17. The GitHub workflow targets JDK 21.

## Run in Eclipse

1. Clone the repository or download and extract its ZIP:

   ```bash
   git clone https://github.com/GoobinJung/POS-System-project.git
   ```

2. Select **File → Import → General → Existing Projects into Workspace**.
3. Select the repository directory and import the top-level **Project 2 POS SYSTEM** project. Leave the nested `src/posPd` project unselected if Eclipse lists it separately.
4. Ensure **JRE System Library** uses JavaSE-21.
5. Open **`src/posUi/PosFrame.java`** and select **Run As → Java Application**.

Use the repository root as the working directory so the application can locate `StoreData.csv`. In Eclipse this is under **Run Configurations → Arguments → Working directory**.

`PosFrame` starts the desktop application. `PosStart` is an earlier console demonstration.

## Run from PowerShell

From the repository root:

```powershell
$sources = Get-ChildItem -Path src -Recurse -Filter *.java |
    ForEach-Object { $_.FullName }

javac -encoding UTF-8 -d out $sources
java -cp out posUi.PosFrame
```

## Demo Walkthrough

1. Choose **POS → Login / Return to checkout**.
2. Select a demo cashier:

   | Cashier | Name | Password |
   |---|---|---|
   | 1 | David | `password1` |
   | 2 | Sally | `password2` |

3. Select register **1** or **2**, enter starting cash such as `100.00`, and log in.
4. Enter a quantity and a product UPC, then press **Enter** in the UPC field or choose **Add item**.

   | UPC | Product |
   |---|---|
   | `11111111111` | Turkey Sandwich |
   | `22222222222` | Ham Sandwich |
   | `33333333333` | Coke |
   | `44444444444` | Dr. Pepper |

5. Select **Payment → Cash**. The exact balance is prefilled; use that amount or a larger cash amount and select **Save**.
6. Select **Payment Complete**, then **Complete Sale**. Only **Complete Sale** records the transaction.
7. After completing or cancelling the current sale, choose **End Session**, enter the counted cash, and finish reconciliation.
8. Use **Reports** to review sales and export CSV files.

Check payments use a fictional nine-digit routing number, a fictional account number, and a check number. Credit payments take a card type, **four demo digits only**, and a non-expired `YYYY-MM-DD` expiration date. No processor or bank is contacted.

## Maintenance Behavior

- End the active cashier session before editing store data.
- Identifiers are fixed after creation. Names and other details can be edited.
- Leave an existing cashier's password field blank to keep the current password.
- New items need at least one UPC and price before they can be saved.
- New tax categories need at least one dated rate.
- Saved UPC, price, and tax-rate edits take effect immediately for existing records. Checkout line items retain their captured values.
- Cashiers/registers with recorded sessions and tax categories assigned to items cannot be deleted.

## Data and Recovery

On first launch, the application imports `StoreData.csv`. Subsequent launches load `StoreState.ser` from the working directory. Local snapshots and generated build files are excluded from Git.

Saves write a temporary file and replace the snapshot atomically where the filesystem supports it. The previous snapshot is kept as `StoreState.ser.bak`. If loading fails, the application reports the error and does not silently replace the saved data with the sample store.

To restore a backup, close the application, keep a copy of the failed snapshot, and copy `StoreState.ser.bak` to `StoreState.ser`. To start over with sample data, move both snapshot files aside before launching.

Completed sales and maintenance changes are saved. An unfinished sale is discarded when closing the application after confirmation; its open cashier session can be resumed after login.

The original CSV has no transaction timestamps, so imported sales use the import time. Its recorded payment amounts are preserved even where they differ from the recomputed sale totals. Reports show sale totals and recorded payments separately in the daily report. Sample promotions are dated in 2025.

This is a **single-user educational application**, not a production payment system. Use fictional data. Local snapshots are not encrypted, and real payment processing, production access controls, refunds, and multi-terminal synchronization are outside its scope.

## Project Structure

| Path | Responsibility |
|---|---|
| `src/posUi/` | Application frame, maintenance forms, checkout, reconciliation, and report screens |
| `src/posPd/` | Stores, cashiers, registers, items, pricing, taxes, sales, payments, sessions, and report calculations |
| `src/posDm/StoreDm.java` | Seed CSV import, filtered snapshot loading, atomic saves, and backups |
| `src/posDm/CsvExport.java` | UTF-8 report export with CSV quoting |
| `src/posTest/RegressionTest.java` | Automated domain, import, persistence, and export checks |
| `src/posUi/WorkflowTest.java` | Swing panel rendering and end-to-end form/checkout checks |
| `src/posTest/StoreTest.java` | Original console inspection utility |
| `test.ps1` | Compile and run both automated suites |
| `.github/workflows/java.yml` | JDK 21 build and regression checks |

## Validation

Run both suites from PowerShell:

```powershell
.\test.ps1
```

Or, after compiling:

```powershell
java -cp out posTest.RegressionTest
java -cp out posUi.WorkflowTest
java -cp out posTest.StoreTest
```

The automated checks cover exact and mixed payments, cash change, duplicate-sale prevention, invalid inputs, promotion boundaries, tax-rate selection, historical amount snapshots, report totals, password verification, CSV import/export, snapshot recovery, maintenance forms, and checkout persistence.

Swing workflow checks run on the event dispatch thread without displaying the frame. In headless environments they render the component panels and skip the frame workflow; the GitHub workflow uses a virtual display to run the full flow.

