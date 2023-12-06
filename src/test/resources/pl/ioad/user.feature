Feature: User Authentication
  Users should be able to register and login with email and password

  Scenario: User login
    Given a login API endpoint
    When the user submits a POST request with login data
      | email    | customer@practicesoftwaretesting.com |
      | password | welcome01                            |
    Then a 200 status is received
    And the response contains access token

  Scenario: User login with invalid credentials
    Given a login API endpoint
    When the user submits a POST request with login data
      | email    | wrong.email@blabla.com |
      | password | wrong.password         |
    Then a 401 status is received

  Scenario: User login with missing credentials
    Given a login API endpoint
    When the user submits a POST request with login data
      | email    |  |
      | password |  |
    Then a 401 status is received

  Scenario: Register new user
    Given a register API endpoint
    When the user submits a POST request with random registration data
    Then a 201 status is received
    Given a login API endpoint
    When the user submits a POST request with random login data
    Then a 200 status is received

  Scenario: Register new user with existing email
    Given a register API endpoint
    When the user submits a POST request with registration data containing existing email
    Then a 422 status is received
    And the response contains error 'email' message with text 'A customer with this email address already exists.'