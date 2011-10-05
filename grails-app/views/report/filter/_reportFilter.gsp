<div class="reportFilter">
  <table>
    <tbody>
      <g:each var="filterParam" in="${filterParams}">
        <g:reportFilterParameter template="${filterParam.getTemplate()}"
                                 model="${filterParam.getModel()}"/>
        </g:each>
    </tbody>
  </table>
</div>