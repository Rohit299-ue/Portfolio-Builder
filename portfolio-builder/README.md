# Portfolio Builder

A full-stack web application for creating professional portfolios with resume parsing and modern design.

## Features

- User authentication (signup/login)
- Upload 2 profile photos (hero & about sections)
- Resume upload with automatic data extraction
- Project showcase
- Download portfolio (HTML, PDF, ZIP)
- Modern dark theme with starry background
- Separate text for hero and about sections

## How to Run

1. **Kill port 8080** (if needed):
   ```
   Right-click KILL-PORT-8080.ps1 → Run with PowerShell
   ```

2. **Start application**:
   ```
   Double-click START-APP.bat
   ```

3. **Open browser**:
   ```
   http://localhost:8080
   ```

## Requirements

- Java 17 or higher
- Maven (included via wrapper)

## Tech Stack

- **Backend**: Spring Boot, Spring Security, JPA
- **Frontend**: Thymeleaf, HTML, CSS, JavaScript
- **Database**: H2 (file-based, persistent storage)
- **Libraries**: Apache Tika (resume parsing), PDFBox (PDF generation)

## Database

- **Type**: H2 Database (file-based)
- **Location**: `data/portfoliodb.mv.db`
- **Persistence**: All data is saved permanently
- **View Database**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:file:./data/portfoliodb`
  - Username: `sa`
  - Password: (empty)

See `DATABASE-INFO.txt` for more details.
