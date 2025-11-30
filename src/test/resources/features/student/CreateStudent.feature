Feature: Create Student

  Background: Switch to Storefront user
    Given Switch to Storefront cockpit guest user.

  Scenario: Check that created booking displays correctly in the table
    Given Switch to Storefront cockpit guest user.
    And Check that created booking data is correct in the table.