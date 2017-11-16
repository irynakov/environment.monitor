import {Component} from "@angular/core";
import {StatusTimeRange} from "../../../shared/model/StatusTimeRange";
import {EnvironmentsService} from "../../../shared/service/environments.service";
import {DateRange} from "../../../shared/model/DateRange";
import * as moment from "moment";
import {Environment} from "../../../shared/model/Environment";
import {environment} from "../../../../environments/environment";
import {Http, Response} from "@angular/http";

@Component({
  moduleId: module.id,
  selector: 'environment-timescale-dashboard',
  templateUrl: './env-timescale-dashboard.component.html',

})
export class EnvironmentTimescaleDashboardComponent {

  public statusTimerange: StatusTimeRange;
  public environments: Environment[];


  constructor(envService: EnvironmentsService, private http: Http) {
    envService.getEnvironments().subscribe(envs => {
        this.environments = envs;
      this.statusTimerange = new StatusTimeRange(new DateRange(moment().startOf('day').toDate(), moment().toDate(), null), envs[0], null)
      }
    );
  }


  /**
   * Set new status range when status range picker selection updates
   * @param statusTimerange
   */
  onStatusRangeChanged(statusTimerange: StatusTimeRange) {

    console.log(`dashboard status range updated:  
          env: ${statusTimerange.environment}
          DateRAnge: ${statusTimerange.daterange.start} - ${statusTimerange.daterange.end}`
    );

    this.statusTimerange = statusTimerange;
  }

  downloadPdf(){

    window.open(`${environment.apiBaseUrl}/report/pdf/aggregated/${this.statusTimerange.environment}?startDate=${moment(new Date(this.statusTimerange.daterange.start).toISOString())}&endDate=${moment(new Date(this.statusTimerange.daterange.end).toISOString())}`);
  }
}
