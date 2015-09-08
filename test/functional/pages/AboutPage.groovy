package pages

import geb.Page

class AboutPage extends LayoutPage  {
    static url = "about"

    static at = {
        heading.text() ==~ /Australian Animal Tracking and Monitoring System (AATAMS)/
    }

    static content = {
    }
}
