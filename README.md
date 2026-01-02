# Cloud Architecture Project – AWS ECS & RDS

This repository contains a **production-ready backend application deployed on Amazon Web Services (AWS)** as part of my hands-on training for the **AWS Cloud Practitioner (CLF-C02)** certification.

The goal of this project is to demonstrate a **real cloud deployment**, covering containerization, managed compute, networking, load balancing, auto scaling, and managed database services.

---

## 🚀 Overview

The application is a **Java backend built with Spring Boot**, packaged as a Docker image and deployed on **Amazon ECS (Fargate)**.  
It exposes REST endpoints through an **Application Load Balancer (ALB)** and persists data in **Amazon RDS (PostgreSQL)**.  
Object storage is handled via **Amazon S3**.

The architecture is designed to be **scalable, fault-tolerant at the application layer**, and aligned with AWS best practices.

---

## 🧱 Architecture Components

The deployment uses the following AWS services:

- **Amazon ECS (Fargate)** – Container orchestration without managing EC2 instances
- **Amazon ECR** – Docker image repository
- **Application Load Balancer (ALB)** – Public entry point and traffic distribution
- **ECS Service Auto Scaling** – Multiple tasks running across Availability Zones
- **Amazon RDS (PostgreSQL, Single-AZ)** – Managed relational database
- **Amazon S3** – Object storage (e.g. profile images)
- **IAM Roles** – Secure access between services (no hardcoded credentials)
- **AWS CloudWatch Logs** – Centralized application logs

---

## 🧩 Architecture Highlights

- Stateless backend containers running on ECS Fargate
- Minimum of **3 running tasks** for high availability at the application layer
- Load-balanced traffic through ALB
- Database connectivity via RDS within the same VPC
- Secure access to S3 using IAM Task Roles
- Environment-based configuration (no secrets in code)

---

## 🛠️ Application Stack

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **PostgreSQL**
- **Docker**
- **AWS SDK v2**

---

## 🔐 Configuration & Security

### Environment Variables

The application relies entirely on environment variables.

- **Local development**: provided via `.env.example`
- **Production**: provided via **ECS Task Definition**

No AWS credentials are stored in the codebase or repository.

An example environment file is provided:

```env example
# Application
SPRING_PROFILES_ACTIVE=local
SERVER_PORT=8080

# Database
SPRING_DATASOURCE_URL=CHANGE_ME
SPRING_DATASOURCE_USERNAME=CHANGE_ME
SPRING_DATASOURCE_PASSWORD=CHANGE_ME

# AWS
AWS_REGION=CHANGE_ME
S3_BUCKET_NAME=CHANGE_ME