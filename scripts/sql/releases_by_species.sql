select count(spcode), spcode as "CAAB code", common_name, scientific_name  from animal_release
join animal on animal_release.animal_id = animal.id
join species on animal.species_id = species.id
group by spcode, common_name, scientific_name
order by count(spcode) desc;

