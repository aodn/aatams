<div class="exportPane">

	<div class="buttons" style="padding-top: 2px; padding-bottom: 2px;">

        <g:hiddenField name="_name" value="${name}"/>
		<g:hiddenField name="_type" value="${type}"/>
        <g:hiddenField name="_file" value="${jrxmlFilename}"/>
        <g:hiddenField name="PDF" value="PDF" />

		<span class="button"> <label style="padding-left: 10px;">Export
				data as:</label> <g:each in="${formats}" var="format">

				<g:actionSubmit name="${format}" 
				                class="PDF" 
				                value="${format}"
					            action="execute" />

			</g:each>

		</span>
	</div>

</div>
