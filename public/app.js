/*
var temp = document.createElement("div");
temp.style.width = "100px";
temp.style.height = "100px";
temp.style.background = "red";
temp.style.color = "white";
temp.innerHTML = "Hello";
*/

/*
<div class="checkbox-detail-container">
<input value="money" type="checkbox" id="moneyCheckbox" onclick="checkboxFunction()">
<label for="money">DURATION</label>
<div class="box money"></div>
</div>
*/

var named_entites = [
    "ORGANIZATION",
    "LOCATION",
    "PERSON",
    "PERCENT",
    "MONEY",
    "DATE",
    "TIME",
    'NATIONALITY',
    'MISC',
    "CAUSE_OF_DEATH",
    'NUMBER',
    'DURATION',
    'COUNTRY',
    'TITLE'
];

var part_of_speech = [
    'NFP',
    'NN',
    'NNS',
    'NNP',
    'NNPS'
];

String.prototype.format = function() {
    let a = this;
    for (let k in arguments) {
      a = a.replace("{" + k + "}", arguments[k])
    }
    return a
  }

for (let i = 0; i < named_entites.length; i++) {
    let checkbox = document.createElement("div");
    checkbox.classList.add("checkbox-detail-container");
    // <input value="organization" type="checkbox" id="organizationCheckbox" onclick="checkboxFunction()"> 
    checkbox.innerHTML = '<input value="' + named_entites[i].toLowerCase() + '" type="checkbox" id="' + named_entites[i].toLowerCase() +
        'Checkbox" checked="true"><label for="' + named_entites[i].toLowerCase() + '">' + named_entites[i] + '</label>';

    let box = document.createElement("div");
    box.classList.add("box");
    box.classList.add(named_entites[i].toLocaleLowerCase());
    checkbox.appendChild(box);
    document.getElementById("ner-related").appendChild(checkbox);
}

for (let i = 0; i < part_of_speech.length; i++) {
    let checkbox = document.createElement("div");
    checkbox.classList.add("checkbox-detail-container");
    // <input value="organization" type="checkbox" id="organizationCheckbox" onclick="checkboxFunction()"> 
    checkbox.innerHTML = '<input value="' + part_of_speech[i].toLowerCase() + '" type="checkbox" id="' + part_of_speech[i].toLowerCase() +
    'Checkbox" checked="true"><label for="' + part_of_speech[i].toLowerCase() + '">' + part_of_speech[i] + '</label>';
    
    let box = document.createElement("div");
    box.classList.add("box");
    box.classList.add(part_of_speech[i].toLocaleLowerCase());
    checkbox.appendChild(box);
    document.getElementById("pos-related").appendChild(checkbox);
}

var viz;
var cypher = "MATCH p1=(a:Article)-[r11:CONTAINS]-(w1:Word)-[r12:IS]->(ps:PartOfSpeech), p2=(a:Article)-[r21:CONTAINS]-(w2:Word)-[r22:IS]->(ne:NamedEntity) where ({0}) and ({1}) return p1,p2 limit {2}";


function draw() {
    var config = {
        container_id: "viz",
        server_url: "bolt://localhost:7686",
        server_user: "neo4j",
        server_password: "neo4j",
        labels: {
            "Artice": {
                caption: "aid"
            },"Word": {
                caption: "word"
            },"NamedEntity": {
                caption: "name"
            },"PartOfSpeech": {
                caption: "name"
            }
        },
        relationships: {
            "IS": {
                "thickness": "weight",
                "caption": false
            },
            "FOLLOWED_BY": {
                "thickness": "weight",
                "caption": false
            },
            "CONTAINS": {
                "thickness": "weight",
                "caption": true
            }
        },
        initial_cypher: "MATCH p1=(a:Article)-[r11:CONTAINS]-(w1:Word)-[r12:IS]->(ps:PartOfSpeech), p2=(a:Article)-[r21:CONTAINS]-(w2:Word)-[r22:IS]->(ne:NamedEntity) return p1,p2 limit 10"
    }

    viz = new NeoVis.default(config);
    viz.render();
}

var NamedEntityQuery;
var PartOfSpeechQuery;

function NumOfNodesFunction() {
    var numberOfNodes = document.getElementById("hop-queryID").value;
              // "MATCH p=(:Article)-[:CONTAINS]-(:Word)-[:IS]->(ne:PartOfSpeech) return p LIMIT "
    
    NamedEntityQuery = ""
    PartOfSpeechQuery = ""
    for (let i=0; i<named_entites.length; i++)
        if (document.getElementById(named_entites[i].toLowerCase()+"Checkbox").checked)
            NamedEntityQuery = NamedEntityQuery + " or ne.name=\'" + named_entites[i] + "\'";
    
    for (let i=0; i<part_of_speech.length; i++)
        if (document.getElementById(part_of_speech[i].toLowerCase()+"Checkbox").checked)
            PartOfSpeechQuery = PartOfSpeechQuery + " or ps.name=\'" + part_of_speech[i] + "\'";
 
    if (numberOfNodes != "")
    {
        let new_cypher = cypher.format(NamedEntityQuery.substring(4),PartOfSpeechQuery.substring(4),numberOfNodes);    
        viz.renderWithCypher(new_cypher)
        console.log(new_cypher)
    }
}

function CypherQueryFunction() {
    let new_query = document.getElementById("queryID").value;
              // "MATCH p=(:Article)-[:CONTAINS]-(:Word)-[:IS]->(ne:PartOfSpeech) return p LIMIT "
    
    if (new_query != "")
    {   
        viz.renderWithCypher(new_query)
        console.log(new_query)
    }
}