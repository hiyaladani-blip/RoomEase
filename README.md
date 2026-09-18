# RoomEase

### One App. Four Walls. Zero Conflicts.

**RoomEase** is a native Android application designed to make shared living easier by bringing **expense management, chore coordination, and household scheduling** into one place.

Instead of relying on separate spreadsheets, notes, reminders, or group chats, RoomEase provides roommates with a shared workspace to manage everyday household responsibilities transparently and collaboratively.

> Built as a Mobile Application Development project using Kotlin and Android Studio.

## Problem Statement
Living with roommates comes with several coordination challenges.
Three common problems are:

###  Financial Disputes
Shared expenses such as groceries, utilities, rent, and subscriptions can become difficult to track. This can lead to confusion about who paid, who owes money, and how much each person should contribute.

### Unequal Chore Distribution
Household responsibilities such as cleaning, cooking, and taking out the trash are often distributed inconsistently. Without a clear system, some roommates may repeatedly end up doing the same tasks.

### Scheduling Conflicts
Important household events such as bill payments, maintenance visits, house meetings, grocery runs, and other events can easily be forgotten when communicated informally.

RoomEase brings these three areas together in a single roommate-focused application.

## Our Solution
RoomEase creates a shared **"House"** where roommates can coordinate their daily responsibilities.
The application consists of three major modules:
*  **Shared Calendar**
*  **Expense Tracker**
*  **Chore Management System**
These modules work together to provide a centralized system for managing a shared household.

# Features
## 1. Shared Calendar
The Shared Calendar provides roommates with a common view of important household events.

### Features
* Full-month calendar view
* Event markers
* Categorized events
* Shared household events
* Event details
* Reminder support
* Events such as bills, meetings, maintenance visits, and grocery runs
The calendar uses color-coded event categories so users can identify different types of events quickly.

# 2. Expense Tracker
The Expense Tracker helps roommates record and divide shared expenses.
Users can:
* Add an expense
* Select the person who paid
* Select participating roommates
* Split expenses equally
* Enter custom amounts
* Use percentage-based splits
* View individual balances
* Track settlement status
* View expenses by date
* View spending insights

# 3. Chore Management System
RoomEase includes a chore rotation system designed to distribute household tasks fairly among roommates.

### Features
* Create household chores
* Assign chores to roommates
* Daily/weekly rotation
* Mark chores as completed
* View pending chores
* View completed chores
* Maintain chore history
* Notify roommates about new assignments


# Tech Stack

Component            - Technology                                                    
Programming Language - Kotlin                                                        
IDE                  - Android Studio                                                
UI                   - XML Layouts + Material Design 3                               
Local Database       - Room Database / SQLite                                        
Navigation           - Jetpack Navigation Component                                  
Architecture         - MVVM                                                          
Reactive UI          - LiveData                                                      
Background Tasks     - WorkManager                                                   
UI Lists             - RecyclerView                                                  
Backend              - Firebase Realtime Database 

The project report identifies Kotlin as the primary language and describes an Android architecture using MVVM, Room, RecyclerView, LiveData, Coroutines, and WorkManager.


# Project Structure

A possible structure for the Android project is:

```text
RoomEase/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com.example.roomease/
│           │       ├── activities/
│           │       ├── fragments/
│           │       ├── adapters/
│           │       ├── models/
│           │       ├── viewmodels/
│           │       ├── database/
│           │       └── utils/
│           │
│           └── res/
│               ├── layout/
│               ├── drawable/
│               ├── values/
│               └── navigation/
│
├── README.md
└── build.gradle
```


# Prerequisites
Make sure you have:
* Android Studio
* Android SDK
* JDK compatible with your project
* An Android device or emulator

# Installation
## 1. Clone the repository
```bash
git clone https://github.com/your-username/roomease.git
```

## 2. Open the project
Open the cloned project in **Android Studio**.

## 3. Sync Gradle
Allow Android Studio to download and configure the required dependencies.

## 4. Run the application
Connect an Android device or start an emulator and click:

##  Author
*Hiya Ladani*

Student | Computer Science Engineering


