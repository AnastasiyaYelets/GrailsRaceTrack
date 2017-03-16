package racetrack
import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*
@Integration
@Rollback





 class RaceIntegrationTests extends Specification {
 void testRaceDatesBeforeToday() {
 def lastWeek = new Date() - 7
 def race = new Race(startDate:lastWeek)
 assertFalse "Validation should not succeed",
 race.validate()
 // It should have errors after validation fails
 assertTrue "There should be errors",
 race.hasErrors()

 println "\nErrors:"
 println race.errors ?: "no errors found"

  def badField = race.errors.getFieldError('startDate')
  println "\nBadField:"
  println badField ?: "startDate wasn't a bad field"
   assertNotNull

   badField

   def code = badField?.codes.find {
   it == 'race.startDate.validator.invalid'
   }
    println "\nCode:"
    println code ?:
    "the custom validator for startDate wasn't found"
 assertNotNull "startDate field should be the culprit",
 code
  }
  }
