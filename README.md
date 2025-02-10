# ElectronicStore-Backend-

# Electronic Store

## Overview
Electronic Store is a **Spring Boot and Angular-based e-commerce platform** with **role-based access control (Admin, User)** using **JWT authentication** and **Spring Security**. It allows users to **browse, add to cart, and purchase products**, while admins can **manage product categories, orders, and analytics**.

## Features
### **User Features:**
- User **registration and authentication** using JWT.
- Browse and **search products**.
- Add products to **cart and place orders**.
- Update user **profile and view order history**.

### **Admin Features:**
- **Manage products, categories, and users**.
- **Approve and track orders**.
- **View analytics and reports**.

## Tech Stack
- **Frontend:** Angular
- **Backend:** Spring Boot
- **Database:** MySQL
- **Security:** Spring Security with JWT
- **ORM:** Spring Data JPA

## API Endpoints
### **Authentication & Authorization**
- `POST /auth/login` - Login and receive JWT token
- `POST /users` - Register a new user

### **Product Management**
- `GET /products` - List all products
- `POST /products` - Add new product (Admin Only)
- `PUT /products/{id}` - Update product (Admin Only)
- `DELETE /products/{id}` - Delete product (Admin Only)

### **Cart & Order Management**
- `POST /cart/add` - Add product to cart
- `GET /cart` - View cart items
- `POST /order` - Place an order

## Installation
### **Backend Setup:**
```sh
# Clone the repository
git clone https://github.com/yourusername/electronic-store.git
cd electronic-store

# Configure database in application.properties

# Run the backend application
mvn spring-boot:run
```

### **Frontend Setup:**
```sh
cd frontend-electronic-store
npm install
ng serve
```

## Future Enhancements
- Implement **payment gateway integration**.
- Add **wishlist functionality**.
- Enable **email notifications** for order updates.

## Contributing
Feel free to fork the repository and submit **pull requests** with improvements or bug fixes!

## License
This project is **MIT licensed**.

