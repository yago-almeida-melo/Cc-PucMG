//VARIAVEIS GLOBAIS
var objCategorias1 = {};
var objCategorias2 = {};
var objCategorias3 = {};

// Aguarda o carregamento completo da página
document.addEventListener('DOMContentLoaded', function() {
  
  // Obtém o formulário e o seletor de categorias
  var form = document.getElementById('filtro');
  var categorySelect = document.getElementById('categorySelect');

  // Adiciona o evento de clique ao botão de Pesquisar
  form.addEventListener('submit', function(event) {
    event.preventDefault(); // Evita que o formulário seja enviado

    // Obtém o valor selecionado do seletor de categorias
    var categoriafiltrada = categorySelect.value;

    // Cria a URL para a página de pesquisa com o parâmetro 'categ' definido como a categoria filtrada
    var url = 'pesquisa.html';
    url += '?categ=' + categoriafiltrada;

    // Redireciona o usuário para a página de pesquisa com a URL construída
    window.location.href = url;
  });
});


async function getProducts() {
  await fetchProducts("https://diwserver.vps.webdock.cloud/products/category/Apparel - Topwear", objCategorias1);
  await fetchProducts("https://diwserver.vps.webdock.cloud/products/category/Apparel - Loungewear and Nightwear", objCategorias2);
  await fetchProducts("https://diwserver.vps.webdock.cloud/products/category/Apparel - Innerwear", objCategorias3);

  //TESTE
  console.log(objCategorias1);
  console.log(objCategorias2);
  console.log(objCategorias3);

  const produto1 = await getHighestRatedProductImage(objCategorias1);
  updateProductCard(produto1, "avaliado1");

  const produto2 = await getHighestRatedProductImage(objCategorias2);
  updateProductCard(produto2, "avaliado2");

  const produto3 = await getHighestRatedProductImage(objCategorias3);
  updateProductCard(produto3, "avaliado3");

  listaProdutos();
  imagensCarrosel();
}

async function fetchProducts(url, obj) {
  const res = await fetch(url);
  const json = await res.json();
  obj.products = json.products;
}

async function getHighestRatedProductImage(apiJSON) {
  const products = apiJSON.products.slice(0, 3); // Obtém os três primeiros produtos

  // Ordena os produtos com base na avaliação em ordem decrescente
  products.sort((a, b) => b.rating.rate - a.rating.rate);

  if (products.length > 0) {
    return products[0]; // Retorna o produto mais bem avaliado
  } else {
    return null; // Retorna null se não houver produtos disponíveis
  }
}

//ATUALIZA O CARD DE PRODUTO MAIS AVALIADO
function updateProductCard(produto, elementId) {
  if (produto) {
    let strAvaliado = document.getElementById(elementId);
    strAvaliado.innerHTML = `<img id="img-avaliado" src="${produto.image}" class="" alt="...">
    <div class="card-body bg-white p-2 border border-bottom">
      <h5 class="card-title">${produto.title}</h5>
      <p class="card-text">$${produto.price},00 <br>Avaliação: ${produto.rating.rate}★</p>`;
  }
}

//FUNÇÃO PARA INSERIR IMAGENS NO CARROSSEL
function imagensCarrosel() {
  var carrosel = document.getElementById("carrosel");
  carrosel.innerHTML = `<div id="carouselExample" class="carousel slide col-md-9 col-sm-10 col-lg-8 mx-lg-auto mx-md-auo my-5 my-lg-0 my-md-0 my-sm-5  mx-auto">
    <div class="carousel-inner p-0">
      <div class="carousel-item active">
        <img id="img-carrosel" src="${objCategorias1.products[1].image}" class=" w-100" alt="...">
      </div>
      <div class="carousel-item">
        <img id="img-carrosel" src="${objCategorias2.products[1].image}" class="w-100" alt="...">
      </div>
      <div class="carousel-item">
        <img id="img-carrosel" src="${objCategorias3.products[0].image}" class="w-100" alt="...">
      </div>
    </div>
    <button class="carousel-control-prev bg-black" type="button" data-bs-target="#carouselExample" data-bs-slide="prev">
      <span class="carousel-control-prev-icon" aria-hidden="true"></span>
      <span class="visually-hidden">Previous</span>
    </button>
    <button class="carousel-control-next bg-black" type="button" data-bs-target="#carouselExample" data-bs-slide="next">
      <span class="carousel-control-next-icon" aria-hidden="true"></span>
      <span class="visually-hidden">Next</span>
    </button>
  </div>`;
}

function listaProdutos() {
  var produtos = document.getElementById("products");
  for (let i = 0; i < 3; i++) {
    produtos.innerHTML += produtoRetornado(objCategorias1.products[i], 1);
    produtos.innerHTML += produtoRetornado(objCategorias2.products[i], 2);
    produtos.innerHTML += produtoRetornado(objCategorias3.products[i], 3);
  }
}

const produtoRetornado = (product, category) => {
  return `
<div id="card-produtos" class="col-md-4 col my-4">
              <div class="card">
                <img id="img-card" src="${product.image}" class="card-img-top border-bottom rounded-top-5" alt="...">
                <div class="card-body mb-0">
                  <h6 class="card-title">${product.title}</h6>
                  <p class="card-text">$${product.price},00 <br>Avaliação: ${product.rating.rate}★</p>
                  <a id="botao-detalhes" href="#" class="btn btn-primary" onclick="redirectDetails(${product.id}, ${category})">Detalhes</a>
                </div>
              </div>
             </div>`;
}

document.addEventListener('DOMContentLoaded', function () {
  getProducts();

  var botoesDetalhes = document.querySelectorAll('#botao-detalhes');
  // Itera sobre cada botão de detalhes e adiciona o evento de clique
  botoesDetalhes.forEach(function (botao) {
    botao.addEventListener('click', function () {
      // Obtém o ID do produto associado ao botão de detalhes
      var idProduto = botao.dataset.id;
      // Redireciona o usuário para a página de detalhes com o ID do produto como parâmetro
      window.location.href = 'detalhes.html?id=' + idProduto;
    });
  });
});

function redirectDetails(id, category) {
  window.location.href = 'detalhes.html?id=' + id + '&category=' + category;
}







