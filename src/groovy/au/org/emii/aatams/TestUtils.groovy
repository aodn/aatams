package au.org.emii.aatams

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

import org.joda.time.*

/**
 * Utility methods common to all tests.
 *
 * @author jburgess
 */
class TestUtils
{
    static setupMessage(controller)
    {
        controller.metaClass.message = { LinkedHashMap args -> return "${args.code}" }
    }

    static def buildReceiver(name) {
        def nameParts = name.split('-')

        def model = ReceiverDeviceModel.buildLazy(modelName: nameParts[0])
        return Receiver.buildLazy(model: model, serialNumber: nameParts[1])
    }

    static def buildTag(name) {
        def nameParts = name.split('-')

        def codeMap = CodeMap.buildLazy(codeMap: "${nameParts[0]}-${nameParts[1]}")
        def tag = Tag.buildLazy(codeMap: codeMap, serialNumber: nameParts[2])
        def sensor = Sensor.buildLazy(tag: tag, pingCode: Integer.valueOf(nameParts[2]))

        return tag
    }

    static def buildReleaseAndSurgery(params) {
        def tag = buildTag(params.tagName)
        def release = AnimalRelease.buildLazy(embargoDate: params.embargoed ? new DateTime().plusYears(10).toDate() : null)
        def surgery = Surgery.buildLazy(release: release, tag: tag)

        return [release: release, surgery: surgery]
    }
}
