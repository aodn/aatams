$(function() 
{
    $('#station\\.id').change(function()
    {
        console.log("Station changed.")
        
        $.post('/aatams/installationStation/show', 
              {'id':$('#station\\.id').val(),
               'encoding':'json'},
              function(data) 
              {
                  console.log(data)
              },
              'json');
    })
});
