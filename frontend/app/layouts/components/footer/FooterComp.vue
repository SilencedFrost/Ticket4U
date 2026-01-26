<template>
  <div class="bg-reactive-dark text-white p-4">
    <div class="container-lg">
      <div class="row">
        <div class="col-6 col-md-3 d-flex flex-column">
          <h4>Events</h4>
          <span v-for="x in 5" :key="x">Lorem ipsum... </span>
        </div>
        <div class="col-6 col-md-3 d-flex flex-column">
          <h4>About Us</h4>
          <span v-for="x in 6" :key="x">Lorem ipsum... </span>
        </div>
        <div class="col-6 col-md-3 d-flex flex-column">
          <h4>Our partners</h4>
          <span v-for="x in 3" :key="x">Lorem ipsum... </span>
        </div>
        <div class="col-6 col-md-3 d-flex flex-column">
          <h4>Lorem ipsum...</h4>
          <span v-for="x in 5" :key="x">Lorem ipsum... </span>
        </div>
      </div>
    </div>
  </div>
</template>
