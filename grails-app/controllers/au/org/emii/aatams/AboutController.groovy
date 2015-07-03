package au.org.emii.aatams

import org.apache.shiro.SecurityUtils

class AboutController  {
    def home =  {
        if (SecurityUtils.subject.isAuthenticated()) {
            redirect(controller:"gettingStarted")
        }
        else {
            render(view:"index")
        }
    }

    def index = {
        render(view:"index")
    }
}
