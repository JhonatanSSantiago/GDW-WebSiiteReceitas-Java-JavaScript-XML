/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function criaAjax(url,dados,funcao){
    let ajax=new XMLHttpRequest();
    ajax.onreadystatechange=funcao;
    ajax.open("GET",url+"?"+dados,true);
    ajax.send();
}
function buscarPorTitulo(cont) {
    nomeR = document.getElementById(cont).value;
    console.log(nomeR);
    criaAjax("http://localhost:8080/ReceitasWeb/controlreceitas","nome="+nomeR,mostrar);
}
function ajax(caminho,funcao) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange=funcao;
    xhttp.open("GET",caminho,true);
    xhttp.send();
}
ajax("http://localhost:8080/ReceitasWeb/controlnomes",iniciarMenu);
function iniciarMenu()
{
    if(this.readyState==4&&this.status==200)
    {
        doc=this.responseXML;
        let titulos=doc.getElementsByTagName("nome");
        let texto="";
        let cont = 1;
        for(let titulo of titulos)
        {
            let nome = titulo.firstChild.nodeValue;
            texto+=`<a href=# id="${cont}" value="${nome}" onclick="buscarPorTitulo(${cont})">`
                    +nome+
                    `</a>`;
            
            cont++;
        }
        document.getElementById("menu").innerHTML=texto;
    }
}

function mostrar() {
   
    if(this.readyState===4&&this.status===200) {
        let raiz=this.responseXML.documentElement;
       // let livros=raiz.getElementsByTagName("livro");
        let texto="";
       // for(let livro of livros)
       // {
            texto+=pegaLivro(raiz);
       // }
        document.getElementById("receita").innerHTML=texto;
    }
}
function pegaLivro(livro)
{
    let texto="<div>";
    let filhos=livro.childNodes;
    for(let filho of filhos) {
        if(filho.nodeType===1)
            texto+="<p><b>"+filho.nodeName+"</b>: "+
                filho.firstChild.nodeValue+"</p>";
        if(filho.hasChildNodes()) 
            pegaReceita(filho);
       // filhosF=filho.childNodes;
      //  for(let filhof of filhosF){
      //      if(filho.nodeType===1)
      //          texto+="<p><b>"
      //  }
    }
    return texto+"</div>";
}
