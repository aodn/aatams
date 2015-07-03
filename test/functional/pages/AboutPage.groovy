package pages

import geb.Page

class AboutPage extends LayoutPage  {
    static url = "about"

    static at = {
        heading.text() ==~ /Australian Animal Tagging and Monitoring System (AATAMS)/
    }

    static content = {
    }
}
