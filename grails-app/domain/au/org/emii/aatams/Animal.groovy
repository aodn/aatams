package au.org.emii.aatams

class Animal 
{
    Species species
    Sex sex
    
    String toString()
    {
        return String.valueOf(species) + ", sex: " + String.valueOf(sex)
    }
    
}
