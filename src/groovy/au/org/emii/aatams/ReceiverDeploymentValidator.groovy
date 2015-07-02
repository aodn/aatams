package au.org.emii.aatams

class ReceiverDeploymentValidator {
    static def conflictingDeploymentValidator = { initialisationDateTime, deployment ->
        // All non-null deployment intervals for receiver, other than 'this' deployment's.
        def conflictingDeployments = deployment.receiver?.deployments.findAll {
            !it.same(deployment) && it.undeployableInterval
        }.findAll {
            def checkInterval = it.undeployableInterval

            def containedBy =
                checkInterval.containsAny([
                    deployment.initialisationDateTime,
                    deployment.deploymentDateTime
                ])

            def overlappedBy = deployment.undeployableInterval?.overlaps(checkInterval)

            return containedBy || overlappedBy
        }

        if (conflictingDeployments.isEmpty()) {
            return true
        }

        [
            'receiverDeployment.initialisationDateTime.conflictingDeployment',
            deployment.receiver,
            conflictingDeployments
        ]
    }
}
