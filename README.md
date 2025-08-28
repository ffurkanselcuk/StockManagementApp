# Stock Management App

A modern inventory and stock management application for Android, designed for small to medium-sized businesses. This app allows for easy tracking of products, suppliers, and stock transactions.

---

## Features

### Core Features

- **User Authentication:** Simple login screen with mocked credentials (`admin`/`password`).
- **Dashboard:** An overview of low-stock items and quick access to main features.
- **Product Management:**
  - Full CRUD operations: List, add, view, edit, and delete products.
  - View detailed product information.
  - Search for products by name.
- **Supplier Management:**
  - Full CRUD operations: List, add, view, edit, and delete suppliers.
  - View detailed supplier information.
  - Search for suppliers by name.
- **Stock Management:** Add stock or record sales directly from the product detail screen.
- **Transaction History:** A chronological list of all stock-in (restock) and stock-out (sale) transactions.
- **Data Validation:** Basic validation for required form fields to prevent empty submissions.

### Bonus Features

- **Data Visualization:** A bar chart on the dashboard displaying the top 5 products with the highest stock levels.
- **Barcode Scanning:** Utilizes the device camera with ML Kit to automatically scan and populate the barcode field when adding or editing a product.
- **Offline-First Support:** The application is designed to work completely offline, with all data stored locally on the device.

---

## Architecture

This project is built upon modern Android application development principles and architectures recommended by Google.

- **Architectural Pattern:** MVVM (Model-View-ViewModel)
- **UI Layer (View):** Fragments & XML, ViewBinding, and the Android Navigation Component for handling the navigation flow.
- **ViewModel Layer:** Uses `androidx.lifecycle.ViewModel` to manage UI-related data and business logic, ensuring data survives configuration changes.
- **Data Layer (Model):**
  - **Repository Pattern:** The `InventoryRepository` class acts as a Single Source of Truth for all data operations.
  - **Local Database:** Room Persistence Library is used for on-device data storage.
  - **Data Models:** Kotlin `data class`es with `@Entity` annotations.
- **Asynchronous Programming:** Uses Kotlin Coroutines and `Flow` to provide a reactive data stream from the database to the UI.
- **Dependency Injection (DI):** Uses Hilt to manage dependencies across the application, improving testability and scalability.

---

## Setup Instructions

1.  Clone this GitHub repository:
    ```bash
    git clone <repository-url>
    ```
2.  Open the project in the latest stable version of Android Studio.
3.  Wait for Gradle to sync all the required dependencies.
4.  Run the application on an emulator or a physical device.
5.  You can use the following mocked credentials to test the application:
    - **Username:** `admin`
    - **Password:** `password`

---

## Design Decisions and Assumptions

- **Offline-First:** The application is designed to be completely offline-first, requiring no internet connection. All data is stored in the device's local Room database. This eliminates the need for a backend service and ensures the app is fast and responsive under all conditions.
- **Mocked Authentication:** The user authentication system is simplified with hardcoded credentials to avoid the need for a complex backend or database table for users.
- **Single Module Structure:** To maintain simplicity, the project was developed within a single `app` module. In a larger-scale project, layers like `data`, `domain`, and `presentation` could be separated into distinct modules.
- **Integrated Stock Management:** The actions for adding stock and recording sales are integrated directly into the `ProductDetailFragment` instead of a separate screen to simplify the user flow.

---

## Known Issues and Limitations

- **No Backend/Sync:** Application data is stored only on the device; there is no cloud synchronization or multi-user support.
- **Limited Reporting:** Data visualization is limited to a simple chart showing the current stock status of top products. Advanced reporting features like detailed sales reports or filtering by date range are not available.
- **Missing Bonus Features:** Some bonus features like data export (to CSV/PDF), push notifications for low stock, and comprehensive unit/UI tests have not yet been implemented.
- **Supplier Relationship:** Although a relationship exists between products and suppliers, the UI for selecting a supplier when adding/editing a product has not been implemented.
