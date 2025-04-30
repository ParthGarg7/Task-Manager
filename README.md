# Task Manager Application

A Java-based desktop task management application with user authentication, task creation, tracking, and management capabilities

## Features

- **User Authentication**: Secure login and registration system
- **Task Management**: Create, edit, and delete tasks
- **Task Organization**: Filter tasks by status (Pending, In Progress, Completed)
- **Dashboard**: Overview of task status with visual indicators
- **Priority Levels**: Assign High, Medium, or Low priority to tasks
- **Due Date Tracking**: Set and monitor task deadlines
- **User Support**: Built-in help system with FAQs and support contact form

## Technical Stack

- **Language**: Java
- **UI Framework**: Java Swing for desktop interface
- **Database**: SQLite for local data storage
- **Architecture**: MVC pattern with DAO layer for data access

## Project Structure

```
src/main/java/com/taskmanager/
├── Main.java                 # Application entry point
├── dao/                      # Data Access Objects
│   ├── TaskDAO.java          # Task database operations
│   ├── UserDAO.java          # User DAO interface
│   └── impl/
│       └── UserDAOImpl.java  # User DAO implementation
├── model/                    # Data models
│   ├── Task.java             # Task entity
│   └── User.java             # User entity
├── ui/                       # User Interface components
│   ├── DashboardUI.java      # Main application dashboard
│   ├── HelpWindow.java       # Help and support interface
│   ├── LoginUI.java          # Login and registration screen
│   └── TaskListUI.java       # Task list and management interface
└── util/                     # Utility classes
    └── DBUtil.java           # Database connection handling
```

## Installation

### Prerequisites

- Java Development Kit (JDK) 11 or higher
- Maven (optional, for building from source)

### Direct Download

1. Download the latest release JAR file from the [releases page](https://github.com/yourusername/taskmanager/releases)
2. Double-click the JAR file or run via command line:
   ```
   java -jar taskmanager.jar
   ```

### Building from Source

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/taskmanager.git
   cd taskmanager
   ```

2. Compile and package the application:
   ```
   # With Maven
   mvn clean package
   
   # Without Maven (using javac)
   mkdir -p target/classes
   javac -d target/classes src/main/java/com/taskmanager/*.java
   jar cfe target/taskmanager.jar com.taskmanager.Main -C target/classes .
   ```

3. Run the application:
   ```
   java -jar target/taskmanager.jar
   ```

## Usage Guide

### First-time Setup

1. Launch the application
2. Create a new account from the Sign Up screen
3. Log in with your new credentials

### Creating Tasks

1. From the dashboard, click "➕ Create New Task"
2. Fill in the task details:
   - Title (required)
   - Description (optional)
   - Due Date (optional)
   - Priority level (High/Medium/Low)
   - Status (default: Pending)
3. Click OK to save the task

### Managing Tasks

1. From the dashboard, click "📋 Manage Tasks"
2. View all your tasks in the list
3. Double-click on any task to edit its details
4. Use the filter dropdown to view tasks by status
5. Select a task and use the buttons to:
   - Change its status
   - Edit details
   - Delete the task

### Getting Help

1. Click on "Help" in the sidebar to access the help center
2. Browse FAQs for common questions
3. Submit a support request via the contact form

## Database Schema

The application uses SQLite with the following tables:

**users**
- user_id (INTEGER, PRIMARY KEY)
- username (TEXT, UNIQUE)
- password_hash (TEXT)

**tasks**
- task_id (INTEGER, PRIMARY KEY)
- user_id (INTEGER, FOREIGN KEY)
- title (TEXT)
- description (TEXT)
- due_date (TEXT)
- status (TEXT)
- priority (TEXT)
- created_at (TEXT)
- updated_at (TEXT)

## Security Features

- Password hashing using SHA-256
- No plaintext password storage
- Input validation on all forms

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Created by Akshat Agrawal, Parth Garg, Manu Yadav, and Manvi Sethi
- Contact information available in the application's Help section

## Future Enhancements
