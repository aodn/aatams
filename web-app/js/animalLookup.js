$(function()
{
    // If any of these three fields change, then we want to update the list
    // of animals.
    $("#speciesName").autocomplete({
        change:function(event, ui)
        {
            updateAnimalList();
        }
    });

    $("#sex\\.id").change(function()
    {
        updateAnimalList();
    });
    
    $("#project\\.id").change(function()
    {
        updateAnimalList();
    });
    
    function updateAnimalList()
    {
        if ($("#speciesName").val() == "")
        {
            // Don't update if species name is blank.
        }
        else
        {
            var projectId = $("#project\\.id option:selected").val();
            var speciesId = $("#speciesId").attr("value");
            var sexId = $("#sex\\.id option:selected").val();

            $.post('/aatams/animal/lookup',
            {
                'project.id':projectId,
                'species.id':speciesId,
                'sex.id':sexId
            },
            function(data)
            {
                // Update the animal select list.
                var animalSelect = $('#animal\\.id');
                animalSelect.empty().append('<option value="">New animal</option>');

                $.each(data, function(i, animal)
                {
                    animalSelect.append($("<option></option>")
                                     .attr("value", animal.id)
                                     .text(animal.name)); 
                });

            },
            'json');
        }
    }
});

