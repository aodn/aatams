package au.org.emii.aatams.notification

class NotificationTagLib  {
    def notification = {
        attrs, body ->

        out << render(template:"/common/notificationTemplate",
                      model:[id:attrs.value.id,
                             key:attrs.value.key,
                             htmlFragment:attrs.value.htmlFragment,
                             anchorSelector:attrs.value.anchorSelector])
    }
}
