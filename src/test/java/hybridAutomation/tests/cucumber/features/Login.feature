Feature: Application Login
@web
  Scenario: Login
  Given User is on Login page
  When User fills the form with username and password and then click on Submit button
  Then User navigated to Home page