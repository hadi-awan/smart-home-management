<h1>Smart Home Management System</h1>
<br>
<h2>Project Overview</h2>
The Smart Home Management System is a web-based application that provides an interface to manage users, homes, and zones within a smart home ecosystem. The system supports different user roles (Admin, Parent, Child, Guest) with specific functionalities and permissions.
<br>
<h2>Features</h2>
<li>User registration and authentication.</li>
<li>Role-based access control (Admin, Parent, Child, Guest).</li>
<li>Management of home zones and appliances.</li>
<li>Observing and managing user statuses (active, parent, child, guest).</li>
<li>RESTful API endpoints for integration.</li>
<br>
<h2>Technologies Used</h2>
<h3>Backend:</h3>
<li>Java 17</li>
<li>Spring Boot (2.7.0 or later):
<ul>
  <li>Spring Data JPA</li>
  <li>Spring Security</li>
  <li>Spring Web</li>
  <li>Spring Validation</li>
</ul>
</li>
<li>Hibernate: ORM for database interactions.</li>
<li>PostgreSQL: Relational database.</li>
<br>
<h3>Frontend:</h3>
<li>Vue.js</li>
<li>Vuetify: Material Design Component Framework.</li>
<br>
<h3>Development Tools:</h3>
<li>Maven: Build tool.</li>
<li>IntelliJ IDEA: IDE.</li>
<li>Postman: API testing.</li>
<br>
<h2>Getting Started</h2>
<h3>Prerequisites</h3>
<li>Java Development Kit (JDK 17)</li>
<li>PostgreSQL Database</li>
<li>Maven</li>
<li>Node.js and npm</li>
<br>
<h3>Installation Steps</h3>
<h4>1. Clone the Repository</h4>
```
git clone https://github.com/hadi-awan/smart-home-management.git
cd smart-home-management
```
<h4>2. Backend Setup</h4>
<li>1. Create a PostgreSQL database named smart_home_management.</li>
<li>2. Update application.properties in src/main/resources:</li>
```
spring.datasource.url=jdbc:postgresql://localhost:5432/smart_home_management
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update
```
<li>3. Build and run the backend:</li>
```
mvn clean install
mvn spring-boot:run
```
The backend server will run at http://localhost:8080.

<h4>3. Frontend Setup</h4>
<li>Navigate to the frontend directory:</li>
```
cd frontend
```
<li>2. Install dependencies:</li>
```
npm install
```
<li>3. Run the development server:</li>
```
npm run serve
```
The frontend server will run at http://localhost:8081 (default).





