
#  🚗 RentaGo AI Car Renting System

### RentaGo is a Java-based CLI car rental system integrated with AI-powered car suggestions.
### Users can register, log in, and rent cars. The AI assistant suggests the best car for a journey based on the list of available cars stored in a file.

<br>

## 📌 Features

<ul>
<li>👤 User Management (Register, Login, Search, Update, Delete users)</li>
<li>🚘 Car Rentals with file-based storage</li>
<li>🤖 AI Assistant that suggests the best car for a journey</li>
<li>🔒 Role-Based Access Control (Admin vs User)</li>
<li>📂 File-based persistence (session.txt, rentals.txt, etc.)</li>
</ul>

```bash

RentaGo/
├── src/
│   ├── Main.java                # Entry point
│   ├── controllers/
│   │   ├── ManageUser.java      # User management (login, register, etc.)
│   │   └── store/session.txt    # Stores active session info
│   ├── services/
│   │   └── AIServices.java      # AI-powered car suggestion logic
│   ├── models/
│   │   └── Car.java             # Car model (optional, if used)
│   └── CarRentals.java          # Rental system logic
├── store_info/
│   └── rentals.txt              # Database of available cars
├── bin/                         # Compiled .class files
└── README.md                    # Project documentation

```

## 🏗️ Setup & Installation

### 1. Clone the Repository

```bash

git clone https://github.com/lokeshwardeb/RentaGo
cd rRentaGo

```

### 2. Add your car data
Edit store_info/rentals.txt and add cars in the following format:

### 3. Compile the project

```bash

javac -d bin src/*.java src/services/*.java src/models/*.java src/controllers/*.java

```

### 4. Run The Program

```bash

java -cp bin Main


```

### Alternatively you can use this command to run and compile your project :

```bash

javac -d bin src/*.java src/services/*.java src/models/*.java src/controllers/*.java && java -cp bin Main

```

## AI Integrations
This software has integrated an powerful Artificial Intellegence (AI) to make the users easier to choose their car for renting using there travel informations. This AI eaisily helps the users to choose the right car for renting.







Use this command to run the project
javac -d bin src/*.java src/services/*.java src/models/*.java src/controllers/*.java && java -cp bin Main

javac -d bin src/*.java src/services/*.java src/models/*.java src/controllers/*.java
