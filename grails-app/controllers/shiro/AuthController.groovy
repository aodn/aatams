package shiro

import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.web.util.SavedRequest
import org.apache.shiro.web.util.WebUtils

import au.org.emii.aatams.EmbargoService
import au.org.emii.aatams.EntityStatus
import au.org.emii.aatams.Person

class AuthController {
    def shiroSecurityManager

    def embargoService

    def index = { redirect(action: "login", params: params) }

    def login = {
        return [ username: params.username, rememberMe: (params.rememberMe != null), targetUri: params.targetUri ]
    }

    def signIn = {
        def authToken = new UsernamePasswordToken(params.username.toLowerCase(), params.password as String)

        // Support for "remember me"
        if (params.rememberMe) {
            authToken.rememberMe = true
        }

        // If a controller redirected to this page, redirect back
        // to it. Otherwise redirect to the root URI.
        def targetUri = params.targetUri ?: "/"

        // Handle requests saved by Shiro filters.
        def savedRequest = WebUtils.getSavedRequest(request)
        if (savedRequest) {
            targetUri = savedRequest.requestURI - request.contextPath
            if (savedRequest.queryString) targetUri = targetUri + '?' + savedRequest.queryString
        }

        try
        {
            // Don't let PENDING users log in.
            checkForPendingUser(params)

            // Perform the actual login. An AuthenticationException
            // will be thrown if the username is unrecognised or the
            // password is incorrect.
            doLogin(authToken)

            embargoService.clearCache()

            redirectToTargetUrl(targetUri)
        }
        catch (AuthenticationException ex){
            // Authentication failed, so display the appropriate message
            // on the login page.
            log.info "Authentication failure for user '${params.username}'."
            flash.message = message(code: "login.failed")

            // Keep the username and "remember me" setting so that the
            // user doesn't have to enter them again.
            def m = [ username: params.username ]
            if (params.rememberMe) {
                m["rememberMe"] = true
            }

            // Remember the target URI too.
            if (params.targetUri) {
                m["targetUri"] = params.targetUri
            }

            // Now redirect back to the login page.
            redirect(action: "login", params: m)
        }
    }

    private redirectToTargetUrl(targetUri)
    {
        log.info "Redirecting to '${targetUri}'."

        // Sometimes we are provided with a full URL (starting with "http(s)://"), which needs a slightly
        // different call to redirect (url instead of uri).
        if (targetUri =~ /^https?:\/\//)
        {
            redirect(url: targetUri)
        }
        else
        {
            redirect(uri: targetUri)
        }
    }

    private doLogin(UsernamePasswordToken authToken) {
        SecurityUtils.subject.login(authToken)
    }

    private checkForPendingUser(Map params) {
        if (Person.findByUsername(params.username)?.status == EntityStatus.PENDING)
        {
            throw new AuthenticationException("Attempted login by PENDING user: " + params.username)
        }
    }

    def signOut = {
        // Log the user out of the application.
        SecurityUtils.subject?.logout()

        embargoService.clearCache()

        // For now, redirect back to the home page.
        redirect(uri: "/")
    }

    def unauthorized = {
        render "You do not have permission to access this page."
    }
}
