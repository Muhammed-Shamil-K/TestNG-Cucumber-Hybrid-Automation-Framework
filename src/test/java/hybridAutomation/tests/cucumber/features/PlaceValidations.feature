Feature: Place API Validation
  Scenario: Verify add place
    Given User has "AddPlace" payload
    When User calls "AddPlace" API as "POST" request with the payload
    Then API call is success with status code as "200"
    And "status" in response is "OK"
    And "scope" in response is "APP"
    And "place_id" is returned in the response
  Scenario: Verify get Place
    Given User has "GetPlace" payload
    When User calls "GetPlace" API as "GET" request with the payload
    Then API call is success with status code as "200"
    And "phone_number" in response is ""
    And "language" in response is "French-IN"
  Scenario: Verify delete Place
    Given User has "DeletePlace" payload
    When User calls "DeletePlace" API as "DELETE" request with the payload
    Then API call is success with status code as "200"
    And "status" in response is "OK"
