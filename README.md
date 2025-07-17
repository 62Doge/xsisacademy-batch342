# Batch 342 Group A Mini Project

This project contains a mini project from group 342 in the form of a healthcare service application.
Member group :
1. Ruri
2. Muti
3. Sukron
4. Ali
5. Ical

This is a mini project I developed together with my team during a bootcamp at Xsis Academy. Inspired by health service apps like Halodoc, we designed features based on user stories provided across two sprints.

With a team of five members, each person was assigned 4 stories (tasks) to complete within 2 weeks (2 sprints in total). I was responsible for 2 Master Data stories and 2 Transactional Data stories:

Master Data

-Location Level
-Payment Method

Transactional Data

-Profile Tab
-Registration

Master Data
1. Location Level
Used to manage administrative area data such as Cities, Provinces, and Regencies.

This module supports full CRUD operations and includes validations such as:

Required fields

Duplicate location level name check

Cannot delete if the data is still in use

2. Payment Method
Used to manage available payment options like QRIS, Debit/Credit Card, and Virtual Account.

This module also supports CRUD operations and validations:

Required fields

Duplicate payment method name check

Cannot delete if the method is still in use

Transactional Data
1. Profile Tab
Used to display and update user (patient) profile data, both personal and account information.

To update an email address, OTP verification is required for the new email. For this feature, I used Mailtrap to simulate email delivery for testing purposes.

Validations include:

Required fields

Email format

Password

OTP code

2. Registration
Used for registering new patient and doctor accounts. Users are required to provide an email, password, and phone number.

An OTP code will be sent to the provided email for verification.

Validations include:

Required fields

Email format

Password

OTP code

Duplicate email check
