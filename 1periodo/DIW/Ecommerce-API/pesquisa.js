let objTopwear = {};
let objLoungeAndNight = {};
let objInnerW = {};

function getURLParameter(name) {
  name = name.replace(/[[\]]/g, '\\$&');
  var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)');
  var results = regex.exec(window.location.href);
  if (!results) return null;
  if (!results[2]) return '';
  return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

document.addEventListener('DOMContentLoaded', function() {
  getProdutos();
});

const listaPesquisa = (obj, i, category) => {
    return `
    <div id="card-produtos" class="col-md-4 col my-4">
              <div class="card">
                <img id="img-card" src="${obj.products[i].image}" class="card-img-top border-bottom rounded-top-5" alt="...">
                <div class="card-body mb-0">
                  <h6 class="card-title">${obj.products[i].title}</h6>
                  <p class="card-text">$${obj.products[i].price},00 <br>Avaliação: ${obj.products[i].rating.rate} de 5</p>
                  <a onclick="redirectDetails(${obj.products[i].id},${category})" class="btn btn-primary">Detalhes</a>
                </div>
              </div>
             </div>`;
}
  
async function getProdutos() {

    //Topwear
    let topwear = await fetch("https://diwserver.vps.webdock.cloud/products/category/Apparel - Topwear");
    let json1 = await topwear.json();
    objTopwear = json1;
  
    //Lounge and Night
    let loungeAndNight = await fetch("https://diwserver.vps.webdock.cloud/products/category/Apparel - Loungewear and Nightwear");
    let json2 = await loungeAndNight.json();
    objLoungeAndNight = json2;
  
    //Innerwear
    let innerW = await fetch("https://diwserver.vps.webdock.cloud/products/category/Apparel - Innerwear");
    let json3 = await innerW.json();
    objInnerW = json3;

    console.log(objTopwear);
    console.log(objLoungeAndNight);
    console.log(objInnerW);

    var searchValue = getURLParameter('search');
    verifProdutosPesquisa(searchValue);

    var filterValue = getURLParameter('categ');
    console.log(filterValue);
    verifProdutosFiltro(filterValue);
}
  
  
  // TRATAMENTO PESQUISA DO HEADER
function  verifProdutosPesquisa(searchValue){
  let rowProdutos = document.getElementById('rowProdutos');
  
  //INSERE O TOPWEAR DA PESQUISA NA TELA
  let foundIndexTitleTopwear = [];

  // Verifica a palavra pesquisada
  objTopwear.products.forEach(function(obj, index) {
    if (obj.title.includes(searchValue)) {
      foundIndexTitleTopwear.push(index);
    }
  });

  //Exibe os Encontrados
    foundIndexTitleTopwear.forEach(function(index) {
      rowProdutos.innerHTML += listaPesquisa(objTopwear, index, 1);
    });


  /* ========================================================================== */

  //INSERE AS LOUNGE AND NIGHTWEAR DA PESQUISA NA TELA
  let foundIndexTitleLoungeAndNight = [];
  
  // Verifica a palavra pesquisada
  objLoungeAndNight.products.forEach(function(obj, index) {
    if (obj.title.includes(searchValue)) {
      foundIndexTitleLoungeAndNight.push(index);
    }
  });

  //Exibe os Encontrados
    foundIndexTitleLoungeAndNight.forEach(function(index) {
      rowProdutos.innerHTML += listaPesquisa(objLoungeAndNight, index, 2);
    });


  /* ========================================================================== */

  //INSERE OS INNERWEAR DA PESQUISA NA TELA
  let foundIndexTitleInnerW = [];

  // Verificar palavra pesquisa
  objInnerW.products.forEach(function(obj, index) {
    if (obj.title.includes(searchValue)) {
      foundIndexTitleInnerW.push(index);
    }
  });

  //Exibe os Encontrados    
    foundIndexTitleInnerW.forEach(function(index) {
      rowProdutos.innerHTML += listaPesquisa(objInnerW, index, 3);
    });
}

//------------------------------------------------------------------------------------------------------//
//-----------------------------------TRATAMENTO FILTRO DO HEADER----------------------------------------//
//------------------------------------------------------------------------------------------------------//

function  verifProdutosFiltro(filterValue){
  let rowProdutos = document.getElementById('rowProdutos');
  
  //INSERE O TOPWEAR DA PESQUISA NA TELA
  let foundIndexTitleTopwear = [];
  
  // Verifica a palavra pesquisada
  objTopwear.products.forEach(function(obj, index) {
    if (obj.category == filterValue) {
      foundIndexTitleTopwear.push(index);
    }
  });

  //Exibe os Encontrados
    foundIndexTitleTopwear.forEach(function(index) {
      rowProdutos.innerHTML += listaPesquisa(objTopwear, index, 1);
    });


  /* ========================================================================== */

  //INSERE AS LOUNGE AND NIGHTWEAR DA PESQUISA NA TELA
  let foundIndexTitleLoungeAndNight = [];
  
  // Verifica a palavra pesquisada
  objLoungeAndNight.products.forEach(function(obj, index) {
    if (obj.category == filterValue) {
      foundIndexTitleLoungeAndNight.push(index);
    }
  });

  //Exibe os Encontrados
    foundIndexTitleLoungeAndNight.forEach(function(index) {
      rowProdutos.innerHTML += listaPesquisa(objLoungeAndNight, index, 2);
    });


  /* ========================================================================== */

  //INSERE OS INNERWEAR DA PESQUISA NA TELA
  let foundIndexTitleInnerW = [];

  // Verificar palavra pesquisa
  objInnerW.products.forEach(function(obj, index) {
    if (obj.category == filterValue) {
      foundIndexTitleInnerW.push(index);
    }
  });

  //Exibe os Encontrados    
    foundIndexTitleInnerW.forEach(function(index) {
      rowProdutos.innerHTML += listaPesquisa(objInnerW, index, 3);
    });
}

function redirectDetails(id, category) {
  window.location.href = 'detalhes.html?id=' + id + '&category=' + category;
}
  
  