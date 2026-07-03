# 🏦 Simple Banking Application

This is my School term project for Java lab. I built it when I am taking Java OOP. It is a robust, desktop-based retail banking application built using **Java (Swing GUI)** and **Microsoft SQL Server**. This project serves as an academic term project implementing a clean 3-Tier Architecture to demonstrate Object-Oriented Programming (OOP) concepts, rigorous exception handling, and database normalization patterns.

---

## 🚀 Key Features

- **User Authentication:** Secure registration and login portals for account owners.
- **Core Banking Engine:** Real-time processing for monetary **Deposits** and **Withdrawals**.
- **Inter-Account Transfers:** Atomic funds transfers linking sender and receiver accounts seamlessly.
- **Auditing & Accounting:** Dynamic balance inquiries and generation of a complete account transaction statement record.
- **Currency Converter:** Built-in modular utility tool for multi-currency conversion calculations.

---

## 🛠️ Project Architecture

The system is engineered using a **3-Tier Architecture** pattern to isolate concerns and maintain a modular codebase:

```text
Java School lab Project/
│
├── lib/                             # External Native Libraries & JAR Dependencies
│   ├── mssql-jdbc-13.4.0.jre11.jar   # Microsoft JDBC Database Connection Driver
│   └── mssql-jdbc_auth-13.4.0.x64.dll # Native Windows Authentication Library
│
├── src/                             # Application Source Code
│   ├── com/banking/
│   │   ├── model/                   # Object-Oriented Encapsulated Entities
│   │   │   ├── User.java
│   │   │   └── Account.java
│   │   ├── dao/                     # Data Access Objects (SQL Query Layer)
│   │   │   └── UserDAO.java
│   │   ├── ui/                      # Presentation Layer (Java Swing Elements)
│   │   └── util/                    # Infrastructure Utilities (DB Connections)
│   │       └── DatabaseConnection.java
│   │
│   └── SimpleBankingApplication.java # Main Execution Entry Point
└── README.md
```
