
<template>
  <div v-if="this.$store.state.user.id != undefined">
    <tabs>
      <tab name="SHS">
        <simulator></simulator>
      </tab>
      <tab name="SHC">
        <core></core>
      </tab>
      <tab name="SHP">
        <security></security>
      </tab>
      <tab name="SHH">
        <heating></heating>
      </tab>
      <tab name="+">
        <custom></custom>
      </tab>
    </tabs>
  </div>
</template>

<script>
import Vue from 'vue';
import { Tabs, Tab } from 'vue-tabs-component';
import core from './CoreModule';
import security from './SecurityModule';
import heating from './HeatingModule';
import custom from './CustomModule';
import simulator from './SimulatorModule';

Vue.component('tabs', Tabs);
Vue.component('tab', Tab);

export default {
  name: 'modules',
  components: {
    core: core,
    security: security,
    heating: heating,
    custom: custom,
    simulator: simulator
  }
};
</script>

<style>
.tabs-component {
  margin: 4em 0;
}

.tabs-component-tabs {
  border: solid 1px #ddd;
  border-radius: 6px;
  margin-bottom: 5px;
}

@media (min-width: 700px) {
  .tabs-component-tabs {
    border: 0;
    align-items: stretch;
    display: flex;
    justify-content: flex-start;
    margin-bottom: -1px;
  }
}

.tabs-component-tab {
  color: #999;
  font-size: 14px;
  font-weight: 600;
  margin-right: 0;
  list-style: none;
}

.tabs-component-tab:not(:last-child) {
  border-bottom: dotted 1px #ddd;
}

.tabs-component-tab:hover {
  color: #666;
}

.tabs-component-tab.is-active {
  color: #000;
}

.tabs-component-tab.is-disabled * {
  color: #cdcdcd;
  cursor: not-allowed !important;
}

@media (min-width: 700px) {
  .tabs-component-tab {
    background-color: #fff;
    border: solid 1px #ddd;
    border-radius: 3px 3px 0 0;
    margin-right: 0.5em;
    transform: translateY(2px);
    transition: transform 0.3s ease;
  }

  .tabs-component-tab.is-active {
    border-bottom: solid 1px #fff;
    z-index: 2;
    transform: translateY(0);
  }
}

.tabs-component-tab-a {
  align-items: center;
  color: inherit;
  display: flex;
  padding: 0.75em 1em;
  text-decoration: none;
}

.tabs-component-panels {
  padding: 4em 0;
}

@media (min-width: 700px) {
  .tabs-component-panels {
    border-top-left-radius: 0;
    background-color: #fff;
    border: solid 1px #ddd;
    border-radius: 0 6px 6px 6px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
    padding: 4em 2em;
  }
}
</style>
