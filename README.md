# Railway Management System

A full-stack railway ticketing and operations management system featuring a Java Spring Boot REST API backend and a Java Swing desktop application[cite: 1].

---

## 🌟 Key Features

* **Train & Route Search:** Dynamic route-based train search with coach-wise fare calculation and real-time seat tracking[cite: 1].
* **Ticket Booking & Operations:** Ticket lookup, automatic serial-number generation, platform-ticket booking, and passenger-ticket associations[cite: 1].
* **Data Integrity & Validation:** Input verification and validation checks to prevent duplicate train entries and scheduling conflicts[cite: 1].
* **Role-Based Desktop Client:** Java Swing interface featuring:
  * **User Panel:** Train search, passenger profile management, and ticket reservation/retrieval[cite: 1].
  * **Admin Panel:** Train route management, CRUD operations, recent-sales reporting, and train-specific sales analytics[cite: 1].

---

## 🛠️ Tech Stack & Tools

* **Language:** Java 21[cite: 1]
* **Backend Framework:** Spring Boot, Spring Data JPA, Hibernate[cite: 1]
* **Database:** H2 In-Memory Database[cite: 1]
* **Desktop UI:** Java Swing[cite: 1]
* **Build & HTTP Tools:** Maven, Jackson, OkHttp[cite: 1]

---

## 🏗️ Architecture

```text
[ Java Swing Desktop GUI (User/Admin) ]
                 │
           (HTTP / OkHttp)
                 ▼
[ Spring Boot REST Controllers ]
                 │
[ Service Layer & Data Validation ]
                 │
[ Spring Data JPA / Hibernate ORM ]
                 │
           [ H2 Database ]
