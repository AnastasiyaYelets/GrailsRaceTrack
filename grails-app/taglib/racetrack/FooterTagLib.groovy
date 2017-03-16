package racetrack

class FooterTagLib {
def thisYear = {
out << new Date().format("yyyy")
  }
  def copyright = {attrs, body->
  out <<"Copyright "  + attrs.startYear + " - "
  out << thisYear() + " " + body()
  }

    static defaultEncodeAs = [taglib:'html']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]
}
