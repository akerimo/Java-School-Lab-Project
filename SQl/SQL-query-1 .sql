use SimpleBankingAppDB;

-- Users Table: Stores identity only once. John Smith gets a unique UserID = 1. If he changes his name, we update exactly one row in this table.

CREATE TABLE Users (
     UserID INT IDENTITY(1,1) PRIMARY KEY,
     Username VARCHAR(50) UNIQUE NOT NULL,
     Password VARCHAR(255) NOT NULL,
     FullName VARCHAR(100) NOT NULL
);

GO

--Accounts Table: Stores financial state only once. It doesn't care about John's name; it only cares that UserID = 1 owns this account, and tracks the current Balance.

CREATE TABLE Accounts (
    AccountID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL REFERENCES Users(UserID),
    AccountNumber VARCHAR(12) UNIQUE NOT NULL,
    Balance DECIMAL(18,2) NOT NULL DEFAULT 0.00

);

GO

--Transactions Table: Stores single-account history. It records every individual deposit or withdrawal ledger line, pointing back to the account ID.

CREATE TABLE Transactions (
    TransactionID INT IDENTITY(1,1) PRIMARY KEY,
    AccountID INT NOT NULL REFERENCES Accounts(AccountID),
    TransactionType VARCHAR(12) NOT NULL,
    Amount DECIMAL(18,2) NOT NULL,
    TransactionDate DATETIME NOT NULL DEFAULT GETDATE()

);
GO

--Transfers Table: Stores inter-account history. It tracks the complex relationship of money moving from a sender account directly to a receiver account.

CREATE TABLE Transfers (
     TransferID INT IDENTITY(1,1) PRIMARY KEY,
     SendersAccountID INT NOT NULL REFERENCES Accounts(AccountID),
     ReceiverAccountID INT NOT NULL REFERENCES Accounts(AccountID),
     Amount DECIMAL(18,2) NOT NULL,
     TransferDate DATETIME NOT NULL DEFAULT GETDATE()
);