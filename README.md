# 🖥️ Observability & Security Dashboard
### odeXray Intern Evaluation Project (Java + Spring Boot)

This project is a simplified **Observability Microservice** that collects system metrics (CPU & Memory), generates alerts when thresholds are breached, and visualizes them in a live dashboard.

---

## 🚀 Features

✅ Collects system resource metrics:
- CPU usage (%)
- Memory usage (%)

✅ Generates alerts when thresholds are exceeded  
✅ Stores metrics and alerts using JPA (H2/MySQL)  
✅ Exposes secure REST API for summaries  
✅ Interactive Web Dashboard (Chart.js)  
✅ Configurable alert thresholds  
✅ Clean, modular Spring Boot structure  

---

## 🧩 Tech Stack

| Layer | Technology |
|--------|-------------|
| Backend | Java 17, Spring Boot |
| Scheduling | Spring Scheduler |
| Observability | OSHI (system metrics library) |
| Database | H2 / MySQL (configurable) |
| Frontend | HTML, CSS, Chart.js |
| Build Tool | Maven |

---

## ⚙️ Setup Instructions

### 1️⃣ Clone Repository
```bash
git clone https://github.com/Sinchana-ab/odeXray-observability-dashboard.git
cd odeXray-observability-dashboard
2️⃣ Configure Database
In src/main/resources/application.properties:

properties
Copy code
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
(Or configure for MySQL if preferred)

3️⃣ Run Application
bash
Copy code
mvn spring-boot:run
The app will start on http://localhost:8080

🧠 API Endpoints
Endpoint	Method	Description
/api/summary	GET	Returns metrics summary (total alerts, averages)
/api/thresholds	POST	Updates alert thresholds
/api/metrics	GET	(Optional) Returns recent metric readings

🌐 Dashboard
Visit:
👉 http://localhost:8080/dashboard.html

The dashboard displays:

Live CPU & Memory usage trends

Recent alerts

Configurable threshold controls

🧾 Example API Response
json
Copy code
{
  "totalAlerts": 5,
  "alertsByType": {
    "CPU": 3,
    "MEMORY": 2
  },
  "lastAlertTimestamps": [
    "2025-11-05T10:43:21Z",
    "2025-11-05T10:42:56Z"
  ],
  "avgCpu": 47.2,
  "avgMemory": 63.4
}
🧰 Project Structure
cpp
Copy code
com.yourorg.observability
├── controller
│   ├── SummaryController.java
│   └── ThresholdController.java
├── service
│   ├── MetricService.java
│   ├── AlertService.java
│   └── SessionService.java (optional)
├── model
│   ├── MetricReading.java
│   └── Alert.java
├── repository
│   ├── MetricRepository.java
│   └── AlertRepository.java
└── resources/static/dashboard.html
📸 Sample Screenshot

💡 Bonus Features
Configurable thresholds (via UI)

Animated charts (Chart.js)

Optional secure login system (Spring session)

👨‍💻 Author
Your Name
Sinchana A B
🌐https://github.com/Sinchana-ab/