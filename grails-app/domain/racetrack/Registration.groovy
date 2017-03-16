package racetrack

class Registration {

static constraints = {
 race()
  runner()
  paid()
  dateAdded()
}
static belongsTo = [race:Race, runner:Runner]

Boolean paid
Date dateAdded 
}
