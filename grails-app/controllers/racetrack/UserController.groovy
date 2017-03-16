package racetrack

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class UserController {
  def beforeInterceptor = [action:this.&auth2,
  except:['login', 'logout', 'authenticate']]

  def auth2() {
    if(!session.user) {
    redirect(controller:"user", action:"login")
    return false
    }

    if(!session.user.admin){
    flash.message = "Tsk tsk—admins only"
    redirect(controller:"race", action:"index")
    return false
    }
  }

  def login = {}
  def logout = {
    flash.message = "Goodbye ${session.user.login}"
    session.user = null
    redirect(action:"login")
  }
  def authenticate = {
    def user = User.findByLoginAndPassword(
        params.login, params.password.encodeAsSHA())
    if(user){
    session.user = user
    flash.message = "Hello ${user.login}!"
    if(user.admin){
        redirect(controller:"admin", action:"index")
    } else {
        redirect(controller:"race", action:"index")
    }
    }else{
    flash.message =
    "Sorry, ${params.login}. Please try again."
    redirect(action:"login")
    }
  }


  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    respond User.list(params), model:[userCount: User.count()]
  }

    def show(User user) {
    respond user
    }

    def create() {
      respond new User(params)
    }

@Transactional
    def save(User user) {
      if (user == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

if (user.hasErrors()) {
    transactionStatus.setRollbackOnly()
    respond user.errors, view:'create'
    return
}

    user.save flush:true

    request.withFormat {
    form multipartForm {
    flash.message = message(code: 'default.created.message', args: [message(code: 'user.label', default: 'User'), user.id])
    redirect user
}
'*' { respond user, [status: CREATED] }
}
}

def edit(User user) {
respond user
}

@Transactional
def update(User user) {
if (user == null) {
transactionStatus.setRollbackOnly()
notFound()
return
}

if (user.hasErrors()) {
transactionStatus.setRollbackOnly()
respond user.errors, view:'edit'
return
}

user.save flush:true

request.withFormat {
form multipartForm {
flash.message = message(code: 'default.updated.message', args: [message(code: 'user.label', default: 'User'), user.id])
redirect user
}
'*'{ respond user, [status: OK] }
}
}

@Transactional
def delete(User user) {

if (user == null) {
transactionStatus.setRollbackOnly()
notFound()
return
}

user.delete flush:true

request.withFormat {
form multipartForm {
flash.message = message(code: 'default.deleted.message', args: [message(code: 'user.label', default: 'User'), user.id])
redirect action:"index", method:"GET"
}
'*'{ render status: NO_CONTENT }
}
}

protected void notFound() {
request.withFormat {
form multipartForm {
flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
redirect action: "index", method: "GET"
}
'*'{ render status: NOT_FOUND }
}
}
}
