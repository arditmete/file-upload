# **Load File Processor API**

This project provides a Kotlin-based API for processing Load Files commonly used in document processing pipelines. The API supports uploading a ZIP file containing LFP (CSV) and XLF (XML) Load Files, parsing them, and performing validation checks on the extracted data.

## Table of Contents

* Overview
* Installation
* Usage
* API Endpoints
* Validation
* Testing


### Overview

The Load File Processor API is designed to facilitate the parsing and validation of Load Files from ZIP archives. It exposes an endpoint to accept ZIP file uploads, processes the contained LFP and XLF files, and returns the parsed data in a structured format.

### Installation

To deploy the Load File Processor API, follow these steps:

* Clone the repository: git clone https://github.com/arditmete/file-upload.git
* Navigate to the project directory: cd file-upload
* Build the project: ./gradlew build
* Run the application: ./gradlew bootRun
* The API will be accessible at http://localhost:8080.

### Usage

To utilize the API, send a POST request to the /process endpoint with a ZIP file containing LFP and XLF Load Files as the request payload. The API will parse the Load Files, perform validation checks, and return the extracted data.

### API Endpoints

Process ZIP File

* URL: http://localhost:8080/process
* Method: POST
* Request Payload: ZIP file containing LFP and XLF Load Files
* Response: Parsed data including Control Numbers, Volumes, and Paths

### Validation

The API performs the following validation checks:

* Control Numbers: Validates that control numbers start with letters followed by a dash and six numbers.
* ZIP File: Checks if the uploaded file is a valid ZIP archive.
* Image Path Extensions: Verifies that image paths have valid extensions (tif, jpg, png, pdf).

### Testing

The project includes comprehensive tests covering parsing, importing, and validation functionalities. To run the tests, execute: ./gradlew test.