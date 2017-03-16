package racetrack

class LoginTagLib {
  def loginControl = {
    if(session.user){
      out << "Hello ${session.user.login} "
      out << """[${link(action:"logout", controller:"user"){"Logout"}}]"""
    } else {
      out << """[${link(action:"login", controller:"user"){"Login"}}]"""
    }
  }
}
