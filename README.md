## Decisions and Assumptions
- This solution assumes that recipes are already created in the database. When adding or removing recipes, the recipe ID is expected to already exist in the database.
- For testing purposes, the database is recreated from scratch every time the server starts. This is not intended for production use.
- The cart can only have once a product. Attempting to add the same recipe will not result in duplicate products in the cart.
- I've integrated SQLite as the database solution because it's a lightweight, self-contained, and serverless database that I think for the purpose of this test was good enough.
- You can find the DB Schema in file `src/main/resources/schema.sql`
- You can find the pre-populated data on startup in this file `src/main/resources/data.sql`

## Possible Improvements
- As an improvement I would consider changing the endpoint 'POST /carts/{id}/add_recipe' to 'POST /carts/{cartId}/recipes/{recipeId}'
- Due to one of the assumptions, managing quantities in the cart is missing. Something I would also consider if it's needed.

## Running the Server, Tests, and Coverage

### Running the Server

To run the server, execute the following command in your terminal:
```bash
./gradlew bootRun
```
This will start the server, and it will be accessible at `http://localhost:8080`.

### Running Tests

To run the tests, execute the following command in your terminal:
```bash
./gradlew test
```
This will execute all tests in the project.

### Running Coverage

To run coverage, execute the following command in your terminal:
```bash
./gradlew jacocoTestReport
```
This will generate a coverage report in the `build/reports/jacoco` directory. You can view the report by opening the `index.html` file in a web browser.
